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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

enum class VisitFilter {
    ALL,
    VISITED,
    NOT_VISITED,
}

enum class SortOption(
    val label: String,
) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    CODE_ASC("Code (Asc)"),
    CODE_DESC("Code (Desc)"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserListScreen(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(uiState: UserListUiState) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(VisitFilter.ALL) }
    var selectedSort by remember { mutableStateOf(SortOption.NAME_ASC) }

    val userList =
        when (val result = uiState.result) {
            is UserListUiState.Result.Success -> result.users
            else -> emptyList()
        }
    val visitedCount = remember(userList) { userList.count { it.isVisited } }
    val pendingCount = remember(userList) { userList.count { !it.isVisited } }

    // Filter & Sort Logic
    val filteredUsers =
        remember(userList, searchQuery, selectedFilter, selectedSort) {
            userList
                .filter { user ->
                    val matchesQuery =
                        user.name.contains(searchQuery, ignoreCase = true) ||
                            user.code.contains(searchQuery, ignoreCase = true) ||
                            user.address.contains(searchQuery, ignoreCase = true)

                    val matchesStatus =
                        when (selectedFilter) {
                            VisitFilter.ALL -> true
                            VisitFilter.VISITED -> user.isVisited
                            VisitFilter.NOT_VISITED -> !user.isVisited
                        }
                    matchesQuery && matchesStatus
                }.let { list ->
                    when (selectedSort) {
                        SortOption.NAME_ASC -> list.sortedBy { it.name }
                        SortOption.NAME_DESC -> list.sortedByDescending { it.name }
                        SortOption.CODE_ASC -> list.sortedBy { it.code }
                        SortOption.CODE_DESC -> list.sortedByDescending { it.code }
                    }
                }
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                title = {
                    SearchBar()
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
                QuickFilters(uiState)
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
                            UserList(result.users)
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun UserList(users: List<User>) {
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
//                    onUserClick(user)
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
