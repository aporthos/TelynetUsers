package com.telynet.telynetusers.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.core.models.entity.User
import com.telynet.telynetusers.core.ui.ObserveAsEvents
import com.telynet.telynetusers.core.ui.PagedUserList
import kotlinx.coroutines.flow.flowOf

@Composable
fun FavoritesRoute(
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val favorites = viewModel.favorites.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            is FavoritesEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    FavoritesScreen(
        favorites = favorites,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        onUserClick = onUserClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    favorites: LazyPagingItems<User>,
    onIntent: (FavoritesIntent) -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Favorites") })
        },
    ) { innerPadding ->
        PagedUserList(
            users = favorites,
            onUserClick = onUserClick,
            onFavoriteClick = { user -> onIntent(FavoritesIntent.ToggleFavorite(user)) },
            emptyContent = { EmptyFavorites() },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun EmptyFavorites() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(32.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = "No favorites yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Tap the heart on a user to add them here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    val favorites =
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
                            true,
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
        FavoritesScreen(
            favorites = favorites,
            onIntent = {},
            onUserClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    val favorites = remember { flowOf(PagingData.empty<User>()) }.collectAsLazyPagingItems()

    TelynetUsersTheme {
        FavoritesScreen(
            favorites = favorites,
            onIntent = {},
            onUserClick = {},
        )
    }
}
