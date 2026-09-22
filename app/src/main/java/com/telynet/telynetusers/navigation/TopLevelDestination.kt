package com.telynet.telynetusers.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object UsersKey : NavKey

@Serializable
data object FavoritesKey : NavKey

enum class TopLevelDestination(
    val key: NavKey,
    val label: String,
    val icon: ImageVector,
) {
    USERS(UsersKey, "Users", Icons.Default.People),
    FAVORITES(FavoritesKey, "Favorites", Icons.Default.Favorite),
}
