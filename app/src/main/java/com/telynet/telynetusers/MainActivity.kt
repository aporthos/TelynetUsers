package com.telynet.telynetusers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.telynet.telynetusers.core.designsystem.TelynetUsersTheme
import com.telynet.telynetusers.feature.detail.UserDetailActivity
import com.telynet.telynetusers.feature.detail.UserDetailActivity.EXTRA_USER_CODE
import com.telynet.telynetusers.feature.favorites.FavoritesRoute
import com.telynet.telynetusers.feature.users.UserListRoute
import com.telynet.telynetusers.navigation.FavoritesKey
import com.telynet.telynetusers.navigation.TopLevelDestination
import com.telynet.telynetusers.navigation.UsersKey
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelynetUsersTheme {
                TelynetUsersApp()
            }
        }
    }
}

@Composable
private fun TelynetUsersApp() {
    val backStack = rememberNavBackStack(UsersKey)
    val currentKey = backStack.lastOrNull()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentKey == destination.key,
                        onClick = { backStack.navigateToTopLevel(destination.key) },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider =
                entryProvider {
                    entry<UsersKey> {
                        UserListRoute(
                            onUserClick = { user ->
                                Intent(context, UserDetailActivity::class.java).apply {
                                    putExtra(EXTRA_USER_CODE, user.code)
                                    context.startActivity(this)
                                }
                            },
                        )
                    }
                    entry<FavoritesKey> { FavoritesRoute() }
                },
        )
    }
}

private fun NavBackStack<NavKey>.navigateToTopLevel(key: NavKey) {
    if (lastOrNull() == key) return
    while (size > 1) removeAt(lastIndex)
    if (key != UsersKey) add(key)
}
