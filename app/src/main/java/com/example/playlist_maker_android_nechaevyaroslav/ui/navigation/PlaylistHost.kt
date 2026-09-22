package com.example.playlist_maker_android_nechaevyaroslav.ui.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.DetailsScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.FavoritesScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.MainScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.NewPlaylistScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.PlaylistScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.PlaylistsScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SearchScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.screens.SettingsScreen
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistViewModel
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistsViewModel
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.NewPlaylistViewModel
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val TRACK_JSON_ARG = "trackJson"
private const val PLAYLIST_ID_ARG = "playlistId"

@Composable
fun PlaylistHost(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel,
    gson: Gson,
    isDarkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    val colors = LocalPlaylistColors.current

    NavHost(
        navController = navController,
        startDestination = Destination.Main.route,
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        composable(Destination.Main.route) {
            MainScreen(
                onSearchClick = { navController.navigateTo(Destination.Search) },
                onPlaylistsClick = { navController.navigateTo(Destination.Playlists) },
                onFavoritesClick = { navController.navigateTo(Destination.Favorites) },
                onSettingsClick = { navController.navigateTo(Destination.Settings) },
            )
        }
        composable(Destination.Search.route) {
            SearchScreen(
                searchViewModel = searchViewModel,
                navigateToDetailScreen = { track ->
                    navController.navigateToDetails(gson.toJson(track))
                },
                navigateBack = navController::navigateBack,
            )
        }
        composable(Destination.Playlists.route) {
            PlaylistsScreen(
                playlistsViewModel = playlistsViewModel,
                onCreatePlaylistClick = { navController.navigateTo(Destination.NewPlaylist) },
                onPlaylistClick = navController::navigateToPlaylist,
                onBackClick = navController::navigateBack,
            )
        }
        composable(
            route = Destination.PlaylistDetails.route,
            arguments = listOf(
                navArgument(PLAYLIST_ID_ARG) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong(PLAYLIST_ID_ARG) ?: 0L
            val playlistViewModel: PlaylistViewModel = koinViewModel {
                parametersOf(playlistId)
            }
            PlaylistScreen(
                playlistViewModel = playlistViewModel,
                onTrackClick = { track ->
                    navController.navigateToDetails(gson.toJson(track))
                },
                onBackClick = navController::navigateBack,
            )
        }
        composable(Destination.NewPlaylist.route) {
            val newPlaylistViewModel: NewPlaylistViewModel = koinViewModel()
            NewPlaylistScreen(
                viewModel = newPlaylistViewModel,
                onBackClick = navController::navigateBack,
            )
        }
        composable(Destination.Favorites.route) {
            FavoritesScreen(
                playlistsViewModel = playlistsViewModel,
                onTrackClick = { track ->
                    navController.navigateToDetails(gson.toJson(track))
                },
                onBackClick = navController::navigateBack,
            )
        }
        composable(Destination.Settings.route) {
            SettingsScreen(
                onBackClick = navController::navigateBack,
                darkThemeEnabled = isDarkTheme,
                onDarkThemeChange = onDarkThemeChange,
            )
        }
        composable(Destination.Details.route) { backStackEntry ->
            val trackJson = backStackEntry.arguments?.getString(TRACK_JSON_ARG)
            if (trackJson != null) {
                DetailsScreen(
                    track = gson.fromJson(trackJson, Track::class.java),
                    playlistsViewModel = playlistsViewModel,
                    onBackClick = navController::navigateBack,
                )
            }
        }
    }
}

private fun NavController.navigateTo(destination: Destination) {
    navigate(destination.route)
}

private fun NavController.navigateToPlaylist(playlistId: Long) {
    navigate("playlist/$playlistId")
}

private fun NavController.navigateToDetails(trackJson: String) {
    navigate("details/${Uri.encode(trackJson)}")
}

private fun NavController.navigateBack() {
    popBackStack()
}
