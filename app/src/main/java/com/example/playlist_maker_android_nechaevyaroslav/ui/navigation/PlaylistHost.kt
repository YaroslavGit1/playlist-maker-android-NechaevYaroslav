package com.example.playlist_maker_android_nechaevyaroslav.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.DetailsScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.MainScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SearchScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SettingsScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.SearchViewModel
import com.google.gson.Gson

private const val TRACK_JSON_ARG = "trackJson"

@Composable
fun PlaylistHost(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    gson: Gson,
    isDarkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
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
            SearchScreen(
                searchViewModel = searchViewModel,
                navigateToDetailScreen = { track ->
                    navController.navigateToDetails(gson.toJson(track))
                },
                navigateBack = navController::navigateBack,
            )
        }
        composable(PlaylistScreen.Settings.route) {
            SettingsScreen(
                onBackClick = navController::navigateBack,
                darkThemeEnabled = isDarkTheme,
                onDarkThemeChange = onDarkThemeChange,
            )
        }
        composable(PlaylistScreen.Details.route) { backStackEntry ->
            val trackJson = backStackEntry.arguments?.getString(TRACK_JSON_ARG)
            if (trackJson != null) {
                DetailsScreen(
                    track = gson.fromJson(trackJson, Track::class.java),
                    onBackClick = navController::navigateBack,
                )
            }
        }
    }
}

private fun NavController.navigateTo(screen: PlaylistScreen) {
    navigate(screen.route)
}

private fun NavController.navigateToDetails(trackJson: String) {
    navigate("details/${Uri.encode(trackJson)}")
}

private fun NavController.navigateBack() {
    popBackStack()
}
