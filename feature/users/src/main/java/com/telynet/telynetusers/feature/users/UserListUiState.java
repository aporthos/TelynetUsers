package com.telynet.telynetusers.feature.users;

import com.telynet.telynetusers.core.models.entity.User;
import java.util.Collections;
import java.util.List;

public class UserListUiState {
    
    public interface Result {
        class Loading implements Result {
            public static final Loading INSTANCE = new Loading();
            private Loading() {}
        }
        
        class Success implements Result {
            private final List<User> users;
            public Success(List<User> users) {
                this.users = users != null ? users : Collections.emptyList();
            }
            public List<User> getUsers() {
                return users;
            }
        }
        
        class Error implements Result {
            private final String message;
            public Error(String message) {
                this.message = message != null ? message : "Unknown error";
            }
            public String getMessage() {
                return message;
            }
        }
    }

    private final Result result;
    private final String searchQuery;
    private final int filterVisited;
    private final String orderBy;
    private final int totalCount;
    private final int visitedCount;

    public UserListUiState(
            Result result,
            String searchQuery,
            int filterVisited,
            String orderBy,
            int totalCount,
            int visitedCount
    ) {
        this.result = result != null ? result : Result.Loading.INSTANCE;
        this.searchQuery = searchQuery != null ? searchQuery : "";
        this.filterVisited = filterVisited;
        this.orderBy = orderBy != null ? orderBy : "name";
        this.totalCount = totalCount;
        this.visitedCount = visitedCount;
    }

    public static UserListUiState initial() {
        return new UserListUiState(Result.Loading.INSTANCE, "", -1, "name", 0, 0);
    }

    public Result getResult() {
        return result;
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
            Result result,
            String searchQuery,
            Integer filterVisited,
            String orderBy
    ) {
        return new UserListUiState(
                result != null ? result : this.result,
                searchQuery != null ? searchQuery : this.searchQuery,
                filterVisited != null ? filterVisited : this.filterVisited,
                orderBy != null ? orderBy : this.orderBy,
                this.totalCount,
                this.visitedCount
        );
    }

    public UserListUiState withCounts(int totalCount, int visitedCount) {
        return new UserListUiState(result, searchQuery, filterVisited, orderBy, totalCount, visitedCount);
    }
}
