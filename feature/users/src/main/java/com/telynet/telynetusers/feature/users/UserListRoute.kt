package com.telynet.telynetusers.feature.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.core.models.entity.User
import com.telynet.telynetusers.core.ui.PagedUserList
import com.telynet.telynetusers.feature.users.components.QuickFilters
import com.telynet.telynetusers.feature.users.components.SearchBar
import kotlinx.coroutines.flow.flowOf

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
    val users = viewModel.users.collectAsLazyPagingItems()

    UserListScreen(
        modifier = modifier,
        uiState = uiState,
        users = users,
        onIntent = viewModel::processIntent,
        onUserClick = onUserClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListScreen(
    modifier: Modifier = Modifier,
    uiState: UserListUiState,
    users: LazyPagingItems<User>,
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
                PagedUserList(
                    users = users,
                    onUserClick = onUserClick,
                    onFavoriteClick = { user ->
                        onIntent(UserListIntent.ToggleFavorite(user))
                    },
                )
            }
        },
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun UserListScreenPreview() {
    val users =
        remember {
            flowOf(
                PagingData.from(
                    listOf(
                        User("mx1", "John Doe", "john.doe@example.com", "1234567890", false, "Mexico", "Http", "Mexico", false),
                        User("mx2", "Jane Smith", "jane.smith@example.com", "9876543210", true, "Mexico", "Http", "Mexico", true),
                    ),
                ),
            )
        }.collectAsLazyPagingItems()

    TelynetUsersTheme {
        UserListScreen(
            uiState = UserListUiState.initial().withCounts(2, 1),
            users = users,
            onIntent = {},
            onUserClick = {},
        )
    }
}
