package com.telynet.telynetusers.feature.users

/** One-time events that shouldn't be replayed after a configuration change, unlike [UserListUiState]. */
sealed interface UserListEffect {
    data class ShowMessage(
        val message: String,
    ) : UserListEffect
}
