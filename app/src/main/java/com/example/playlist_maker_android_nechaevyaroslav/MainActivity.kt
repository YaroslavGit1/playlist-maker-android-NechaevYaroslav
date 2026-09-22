package com.example.playlist_maker_android_nechaevyaroslav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.playlist_maker_android_nechaevyaroslav.data.settings.ThemeSettings
import com.example.playlist_maker_android_nechaevyaroslav.ui.navigation.PlaylistHost
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.PlaylistmakerandroidNechaevYaroslavTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistsViewModel
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.SearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private val gson = Gson()
    private val themeSettings: ThemeSettings by lazy { (application as MyApplication).provideThemeSettings() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeSettings.isDarkTheme.collectAsState(initial = false)

            PlaylistmakerandroidNechaevYaroslavTheme(darkTheme = isDarkTheme) {
                PlaylistHost(
                    navController = rememberNavController(),
                    searchViewModel = searchViewModel,
                    playlistsViewModel = playlistsViewModel,
                    gson = gson,
                    isDarkTheme = isDarkTheme,
                    onDarkThemeChange = { enabled ->
                        lifecycleScope.launch { themeSettings.setDarkTheme(enabled) }
                    },
                )
            }
        }
    }
}
