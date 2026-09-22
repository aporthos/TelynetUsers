package com.telynet.telynetusers.feature.users;

/**
 * Query and header state for the user list. The users themselves are delivered separately as
 * PagingData by {@link UserListViewModel#getUsers()}, whose load states cover loading and errors.
 */
public class UserListUiState {

    private final String searchQuery;
    private final int filterVisited;
    private final String orderBy;
    private final int totalCount;
    private final int visitedCount;

    public UserListUiState(
            String searchQuery,
            int filterVisited,
            String orderBy,
            int totalCount,
            int visitedCount
    ) {
        this.searchQuery = searchQuery != null ? searchQuery : "";
        this.filterVisited = filterVisited;
        this.orderBy = orderBy != null ? orderBy : "name";
        this.totalCount = totalCount;
        this.visitedCount = visitedCount;
    }

    public static UserListUiState initial() {
        return new UserListUiState("", -1, "name", 0, 0);
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public int getFilterVisited() {
        return filterVisited;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getVisitedCount() {
        return visitedCount;
    }

    public int getNotVisitedCount() {
        return totalCount - visitedCount;
    }

    public UserListUiState copyWith(
            String searchQuery,
            Integer filterVisited,
            String orderBy
    ) {
        return new UserListUiState(
                searchQuery != null ? searchQuery : this.searchQuery,
                filterVisited != null ? filterVisited : this.filterVisited,
                orderBy != null ? orderBy : this.orderBy,
                this.totalCount,
                this.visitedCount
        );
    }

    public UserListUiState withCounts(int totalCount, int visitedCount) {
        return new UserListUiState(searchQuery, filterVisited, orderBy, totalCount, visitedCount);
    }

    /**
     * True when the fields that drive the database query are the same, so counts-only updates
     * don't restart paging.
     */
    public boolean hasSameQueryAs(UserListUiState other) {
        return searchQuery.equals(other.searchQuery)
                && filterVisited == other.filterVisited
                && orderBy.equals(other.orderBy);
    }
}
