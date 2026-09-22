package com.telynet.telynetusers.feature.users.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telynet.telynetusers.feature.users.SortOption
import com.telynet.telynetusers.feature.users.UserListUiState
import com.telynet.telynetusers.feature.users.VisitFilter

@Composable
internal fun QuickFilters(uiState: UserListUiState) {
    val userList =
        when (val result = uiState.result) {
            is UserListUiState.Result.Success -> result.users
            else -> emptyList()
        }

    var selectedSort by remember { mutableStateOf(SortOption.NAME_ASC) }
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    val visitedCount = remember(userList) { userList.count { it.isVisited } }
    val pendingCount = remember(userList) { userList.count { !it.isVisited } }
    var selectedFilter by remember { mutableStateOf(VisitFilter.ALL) }
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
    ) {
        item {
            FilterChip(
                selected = selectedFilter == VisitFilter.ALL,
                onClick = { selectedFilter = VisitFilter.ALL },
                label = { Text("All (${userList.size})") },
                shape = RoundedCornerShape(100.dp),
            )
        }

        item {
            FilterChip(
                selected = selectedFilter == VisitFilter.VISITED,
                onClick = { selectedFilter = VisitFilter.VISITED },
                label = { Text("Visited ($visitedCount)") },
                shape = RoundedCornerShape(100.dp),
            )
        }

        item {
            FilterChip(
                selected = selectedFilter == VisitFilter.NOT_VISITED,
                onClick = { selectedFilter = VisitFilter.NOT_VISITED },
                label = { Text("Pending ($pendingCount)") },
                shape = RoundedCornerShape(100.dp),
            )
        }

        item {
            Box {
                AssistChip(
                    onClick = { isSortMenuExpanded = true },
                    label = {
                        Text(
                            "Sort",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Sort options",
                        )
                    },
                    shape = RoundedCornerShape(100.dp),
                )
                DropdownMenu(
                    expanded = isSortMenuExpanded,
                    onDismissRequest = { isSortMenuExpanded = false },
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                selectedSort = option
                                isSortMenuExpanded = false
                            },
                            leadingIcon =
                                if (selectedSort == option) {
                                    {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                        )
                                    }
                                } else {
                                    null
                                },
                        )
                    }
                }
            }
        }
    }
}
