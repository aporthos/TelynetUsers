package com.telynet.telynetusers.feature.users;

import com.telynet.telynetusers.core.models.entity.User;

public interface UserListIntent {

    class SearchQueryChanged implements UserListIntent {
        private final String query;

        public SearchQueryChanged(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

    class FilterVisitedChanged implements UserListIntent {
        private final int filter;

        public FilterVisitedChanged(int filter) {
            this.filter = filter;
        }

        public int getFilter() {
            return filter;
        }
    }

    class OrderByChanged implements UserListIntent {
        private final String orderBy;

        public OrderByChanged(String orderBy) {
            this.orderBy = orderBy;
        }

        public String getOrderBy() {
            return orderBy;
        }
    }

    class ToggleFavorite implements UserListIntent {
        private final User user;

        public ToggleFavorite(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
