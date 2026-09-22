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
internal fun QuickFilters(
    uiState: UserListUiState,
    onFilterSelected: (VisitFilter) -> Unit,
    onSortSelected: (SortOption) -> Unit,
) {
    val selectedSort = uiState.sort
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    val selectedFilter = uiState.filter
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
                onClick = { onFilterSelected(VisitFilter.ALL) },
                label = { Text("All (${uiState.totalCount})") },
                shape = RoundedCornerShape(100.dp),
            )
        }

        item {
            FilterChip(
                selected = selectedFilter == VisitFilter.VISITED,
                onClick = { onFilterSelected(VisitFilter.VISITED) },
                label = { Text("Visited (${uiState.visitedCount})") },
                shape = RoundedCornerShape(100.dp),
            )
        }

        item {
            FilterChip(
                selected = selectedFilter == VisitFilter.NOT_VISITED,
                onClick = { onFilterSelected(VisitFilter.NOT_VISITED) },
                label = { Text("Pending (${uiState.notVisitedCount})") },
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
                                onSortSelected(option)
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
