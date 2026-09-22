package com.telynet.telynetusers.feature.users;

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
}
