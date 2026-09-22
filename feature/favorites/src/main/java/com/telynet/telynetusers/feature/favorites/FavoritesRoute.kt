package com.telynet.telynetusers.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.core.models.entity.User
import com.telynet.telynetusers.core.ui.UserListItemCard
import com.telynet.telynetusers.core.ui.launchDialer
import com.telynet.telynetusers.core.ui.launchGoogleMaps

@Composable
fun FavoritesRoute(
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        uiState = uiState,
        onUserClick = onUserClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    uiState: FavoritesUiState,
    onUserClick: (User) -> Unit,
    onFavoriteClick: (User) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Favorites") })
        },
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            when (uiState) {
                is FavoritesUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is FavoritesUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                is FavoritesUiState.Success -> {
                    if (uiState.users.isEmpty()) {
                        EmptyFavorites()
                    } else {
                        FavoritesList(
                            users = uiState.users,
                            onUserClick = onUserClick,
                            onFavoriteClick = onFavoriteClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesList(
    users: List<User>,
    onUserClick: (User) -> Unit,
    onFavoriteClick: (User) -> Unit,
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
                onClick = { onUserClick(user) },
                onCallClick = { launchDialer(context, user.phone) },
                onNavigateClick = { launchGoogleMaps(context, user.address) },
                onFavoriteClick = { onFavoriteClick(user) },
                modifier = Modifier.animateItem(),
            )
        }
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
    TelynetUsersTheme {
        FavoritesScreen(
            uiState =
                FavoritesUiState.Success(
                    listOf(
                        User("mx1", "John Doe", "john.doe@example.com", "1234567890", false, "Mexico", "Http", "Mexico", true),
                        User("mx2", "Jane Smith", "jane.smith@example.com", "9876543210", true, "Mexico", "Http", "Mexico", true),
                    ),
                ),
            onUserClick = {},
            onFavoriteClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    TelynetUsersTheme {
        FavoritesScreen(
            uiState = FavoritesUiState.Success(emptyList()),
            onUserClick = {},
            onFavoriteClick = {},
        )
    }
}
