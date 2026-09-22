package com.telynet.telynetusers.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.telynet.telynetusers.core.models.entity.User

/**
 * A list of [UserListItemCard]s backed by Paging 3. It covers the first load, errors with retry,
 * the empty state and the "loading more" footer.
 */
@Composable
fun PagedUserList(
    users: LazyPagingItems<User>,
    onUserClick: (User) -> Unit,
    onFavoriteClick: (User) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = { Text("No users found.") },
) {
    val refresh = users.loadState.refresh
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        when (users.itemCount) {
            0 if refresh is LoadState.Loading -> CircularProgressIndicator()
            0 if refresh is LoadState.Error -> LoadError(refresh.error, users::retry)
            0 if refresh is LoadState.NotLoading -> emptyContent()
            else -> UserItems(users, onUserClick, onFavoriteClick)
        }
    }
}

@Composable
private fun UserItems(
    users: LazyPagingItems<User>,
    onUserClick: (User) -> Unit,
    onFavoriteClick: (User) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = users.itemCount,
            key = users.itemKey { it.code },
        ) { index ->
            val user = users[index] ?: return@items
            UserListItemCard(
                user = user,
                onClick = { onUserClick(user) },
                onCallClick = { launchDialer(context, user.phone) },
                onNavigateClick = { launchGoogleMaps(context, user.address) },
                onFavoriteClick = { onFavoriteClick(user) },
                modifier = Modifier.animateItem(),
            )
        }

        when (val append = users.loadState.append) {
            is LoadState.Loading -> {
                item(key = "append_loading") {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item(key = "append_error") {
                    LoadError(append.error, users::retry)
                }
            }

            is LoadState.NotLoading -> {
                Unit
            }
        }
    }
}

@Composable
private fun LoadError(
    error: Throwable,
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        Text(
            text = error.localizedMessage ?: "Something went wrong",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}
