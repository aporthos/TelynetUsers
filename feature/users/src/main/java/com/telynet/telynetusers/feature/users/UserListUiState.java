package com.telynet.telynetusers.feature.users;

import com.telynet.telynetusers.core.models.entity.User;

import java.util.List;

public interface UserListUiState {

    class Loading implements UserListUiState {
        public static final Loading INSTANCE = new Loading();

        private Loading() {
        }
    }

    class Success implements UserListUiState {
        private final List<User> users;

        public Success(List<User> users) {
            this.users = users;
        }

        public List<User> getUsers() {
            return users;
        }
    }

    class Error implements UserListUiState {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
