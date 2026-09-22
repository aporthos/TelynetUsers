package com.telynet.telynetusers.feature.users

import com.telynet.telynetusers.core.models.entity.User

sealed interface UserListIntent {
    data class SearchQueryChanged(
        val query: String,
    ) : UserListIntent

    data class FilterSelected(
        val filter: VisitFilter,
    ) : UserListIntent

    data class SortSelected(
        val sort: SortOption,
    ) : UserListIntent

    data class ToggleFavorite(
        val user: User,
    ) : UserListIntent
}
