package com.telynet.telynetusers.feature.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.telynet.telynetusers.core.ui.ObserveAsEvents
import com.telynet.telynetusers.core.ui.PagedUserList
import com.telynet.telynetusers.feature.users.components.QuickFilters
import com.telynet.telynetusers.feature.users.components.SearchBar
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onUserClick: (User) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val users = viewModel.users.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is UserListEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    UserListScreen(
        modifier = modifier,
        uiState = uiState,
        users = users,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
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
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        onIntent(UserListIntent.FilterSelected(filter))
                    },
                    onSortSelected = { option ->
                        onIntent(UserListIntent.SortSelected(option))
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

@Preview(showBackground = true)
@Composable
private fun UserListScreenPreview() {
    val users =
        remember {
            flowOf(
                PagingData.from(
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
                            false,
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
                            true,
                        ),
                    ),
                ),
            )
        }.collectAsLazyPagingItems()

    TelynetUsersTheme {
        UserListScreen(
            uiState = UserListUiState(totalCount = 2, visitedCount = 1),
            users = users,
            onIntent = {},
            onUserClick = {},
        )
    }
}
