package com.telynet.telynetusers.feature.favorites

import com.telynet.telynetusers.core.models.entity.User

/** Everything the user can do on the favorites screen. */
sealed interface FavoritesIntent {
    data class ToggleFavorite(
        val user: User,
    ) : FavoritesIntent
}
