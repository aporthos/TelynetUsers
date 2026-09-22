package com.telynet.telynetusers.feature.users

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.core.models.entity.User
import com.telynet.telynetusers.core.ui.UserListItemCard
import com.telynet.telynetusers.feature.users.components.QuickFilters
import com.telynet.telynetusers.feature.users.components.SearchBar

enum class VisitFilter(
    val value: Int,
) {
    ALL(-1),
    VISITED(1),
    NOT_VISITED(0),
    ;

    companion object {
        fun fromValue(value: Int): VisitFilter = entries.firstOrNull { it.value == value } ?: ALL
    }
}

enum class SortOption(
    val label: String,
    val key: String,
) {
    NAME_ASC("Name (A-Z)", "name"),
    NAME_DESC("Name (Z-A)", "name_desc"),
    CODE_ASC("Code (Asc)", "code"),
    CODE_DESC("Code (Desc)", "code_desc"),
    ;

    companion object {
        fun fromKey(key: String): SortOption = entries.firstOrNull { it.key == key } ?: NAME_ASC
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onUserClick: (User) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserListScreen(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::processIntent,
        onUserClick = onUserClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListScreen(
    modifier: Modifier = Modifier,
    uiState: UserListUiState,
    onIntent: (UserListIntent) -> Unit,
    onUserClick: (User) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                title = {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { query ->
                            onIntent(UserListIntent.SearchQueryChanged(query))
                        },
                    )
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
            ) {
                QuickFilters(
                    uiState = uiState,
                    onFilterSelected = { filter ->
                        onIntent(UserListIntent.FilterVisitedChanged(filter.value))
                    },
                    onSortSelected = { option ->
                        onIntent(UserListIntent.OrderByChanged(option.key))
                    },
                )
                when (val result = uiState.result) {
                    is UserListUiState.Result.Loading -> {
                        CircularProgressIndicator()
                    }

                    is UserListUiState.Result.Error -> {
                        Text(
                            text = result.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp),
                        )
                    }

                    is UserListUiState.Result.Success -> {
                        if (result.users.isEmpty()) {
                            Text("No users found.")
                        } else {
                            UserList(result.users, onUserClick)
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(users, key = { it.code }) { user ->
            UserListItemCard(
                user = user,
                onClick = {
                    onUserClick(user)
                },
                onCallClick = {
                    launchDialer(context, user.phone)
                },
                onNavigateClick = {
                    launchGoogleMaps(context, user.address)
                },
            )
        }
    }
}

fun launchDialer(
    context: Context,
    phoneNumber: String,
) {
    val intent =
        Intent(Intent.ACTION_DIAL).apply {
            data = "tel:${phoneNumber.replace(Regex("[^0-9+]"), "")}".toUri()
        }
    context.startActivity(intent)
}

fun launchGoogleMaps(
    context: Context,
    address: String,
) {
    val uri = "geo:0,0?q=${Uri.encode(address)}".toUri()
    val mapIntent =
        Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://maps.google.com/?q=${Uri.encode(address)}".toUri(),
            ),
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun UserListPreview() {
    TelynetUsersTheme {
        UserList(
            onUserClick = {},
            users =
                listOf(
                    User(
                        "mx1",
                        "John Doe",
                        "john.doe@example.com",
                        "1234567890",
                        false,
                        "Mexico",
                        "Http",
                        "Mexico",
                    ),
                    User(
                        "mx2",
                        "Jane Smith",
                        "jane.smith@example.com",
                        "9876543210",
                        true,
                        "Mexico",
                        "Http",
                        "Mexico",
                    ),
                ),
        )
    }
}
