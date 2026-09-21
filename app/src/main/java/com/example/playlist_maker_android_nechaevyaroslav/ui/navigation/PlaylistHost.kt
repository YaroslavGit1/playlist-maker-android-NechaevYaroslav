package com.example.playlist_maker_android_nechaevyaroslav.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.MainScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SearchScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SettingsScreen

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = PlaylistScreen.Main.route,
    ) {
        composable(PlaylistScreen.Main.route) {
            MainScreen(
                onSearchClick = { navController.navigateTo(PlaylistScreen.Search) },
                onSettingsClick = { navController.navigateTo(PlaylistScreen.Settings) },
            )
        }
        composable(PlaylistScreen.Search.route) {
            SearchScreen(onBackClick = navController::navigateBack)
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(onBackClick = navController::navigateBack)
        }
    }
}

private fun NavController.navigateTo(screen: PlaylistScreen) {
    navigate(screen.route)
}

private fun NavController.navigateBack() {
    popBackStack()
}
