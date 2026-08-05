package com.ratanapps.exoplayersample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ratanapps.exoplayersample.ui.feature_home.HomeScreen
import com.ratanapps.exoplayersample.ui.feature_home.HomeViewModel
import com.ratanapps.exoplayersample.ui.feature_library.LibraryScreen
import com.ratanapps.exoplayersample.ui.feature_player.PlayerScreen
import com.ratanapps.exoplayersample.ui.feature_player.PlayerViewModel
import com.ratanapps.exoplayersample.ui.feature_search.SearchScreen
import com.ratanapps.exoplayersample.ui.navigation.Screen
import com.ratanapps.exoplayersample.ui.navigation.bottomNavItems

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onTrackClick = { track ->
                            playerViewModel.playTrack(track)
                            navController.navigate(Screen.Player.route)
                        }
                    )
                }
                composable(Screen.Search.route) {
                    SearchScreen(
                        onTrackClick = { track ->
                            playerViewModel.playTrack(track)
                            navController.navigate(Screen.Player.route)
                        }
                    )
                }
                composable(Screen.Library.route) {
                    LibraryScreen(
                        onTrackClick = { track ->
                            playerViewModel.playTrack(track)
                            navController.navigate(Screen.Player.route)
                        }
                    )
                }
                composable(Screen.Player.route) {
                    PlayerScreen(
                        viewModel = playerViewModel,
                        onMinimize = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}