package com.telynet.telynetusers.feature.detail;

import com.telynet.telynetusers.core.models.entity.User;

public interface UserDetailUiState {

    class Loading implements UserDetailUiState {
        public static final Loading INSTANCE = new Loading();
        private Loading() {}
    }

    class Success implements UserDetailUiState {
        private final User user;

        public Success(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    class NotFound implements UserDetailUiState {
        public static final NotFound INSTANCE = new NotFound();
        private NotFound() {}
    }

    class Error implements UserDetailUiState {
        private final String message;

        public Error(String message) {
            this.message = message != null ? message : "Unknown error";
        }

        public String getMessage() {
            return message;
        }
    }
}
