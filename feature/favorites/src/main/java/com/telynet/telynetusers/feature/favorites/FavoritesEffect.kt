package com.telynet.telynetusers.feature.favorites

/** One-time events that shouldn't be replayed after a configuration change. */
sealed interface FavoritesEffect {
    data class ShowMessage(
        val message: String,
    ) : FavoritesEffect
}
