package com.telynet.telynetusers.feature.favorites

import com.telynet.telynetusers.core.models.entity.User

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data class Success(
        val users: List<User>,
    ) : FavoritesUiState

    data class Error(
        val message: String,
    ) : FavoritesUiState
}
