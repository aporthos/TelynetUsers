package com.telynet.telynetusers.feature.users

data class UserListUiState(
    val searchQuery: String = "",
    val filter: VisitFilter = VisitFilter.ALL,
    val sort: SortOption = SortOption.NAME_ASC,
    val totalCount: Int = 0,
    val visitedCount: Int = 0,
) {
    val notVisitedCount: Int get() = totalCount - visitedCount
    internal val query: UserListQuery get() = UserListQuery(searchQuery, filter, sort)
}

internal data class UserListQuery(
    val searchQuery: String,
    val filter: VisitFilter,
    val sort: SortOption,
)

enum class VisitFilter(
    val value: Int,
) {
    ALL(-1),
    VISITED(1),
    NOT_VISITED(0),
}

enum class SortOption(
    val label: String,
    val key: String,
) {
    NAME_ASC("Nombre (A-Z)", "name"),
    NAME_DESC("Nombre (Z-A)", "name_desc"),
    CODE_ASC("Codigo (Asc)", "code"),
    CODE_DESC("Codigo (Desc)", "code_desc"),
}
