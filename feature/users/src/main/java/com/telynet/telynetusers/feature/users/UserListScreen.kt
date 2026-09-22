package com.telynet.telynetusers.feature.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.core.models.entity.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.processIntent(UserListIntent.SearchQueryChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search users by name...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.processIntent(UserListIntent.SearchQueryChanged("")) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.filterVisited == -1,
                        onClick = { viewModel.processIntent(UserListIntent.FilterVisitedChanged(-1)) },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = uiState.filterVisited == 1,
                        onClick = { viewModel.processIntent(UserListIntent.FilterVisitedChanged(1)) },
                        label = { Text("Visited") }
                    )
                    FilterChip(
                        selected = uiState.filterVisited == 0,
                        onClick = { viewModel.processIntent(UserListIntent.FilterVisitedChanged(0)) },
                        label = { Text("Not Visited") }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort by:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    FilterChip(
                        selected = uiState.orderBy == "name",
                        onClick = { viewModel.processIntent(UserListIntent.OrderByChanged("name")) },
                        label = { Text("Name") }
                    )
                    FilterChip(
                        selected = uiState.orderBy == "code",
                        onClick = { viewModel.processIntent(UserListIntent.OrderByChanged("code")) },
                        label = { Text("Code") }
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
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
    }
}

@Composable
fun UserList(users: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(users) { user ->
            UserItem(user)
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = user.code,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (user.phone != null && user.phone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Phone: ${user.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            val badgeBgColor = if (user.isVisited) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val badgeTextColor = if (user.isVisited) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            
            Box(
                modifier = Modifier
                    .background(
                        color = badgeBgColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (user.isVisited) "Visited" else "Not Visited",
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeTextColor
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun UserListPreview() {
    TelynetUsersTheme {
        UserList(
            users =
                listOf(
                    User("mx1", "John Doe", "john.doe@example.com", "1234567890", false),
                    User("mx2", "Jane Smith", "jane.smith@example.com", "9876543210", true),
                ),
        )
    }
}
