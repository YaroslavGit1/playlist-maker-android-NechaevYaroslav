package com.example.playlist_maker_android_nechaevyaroslav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.Black
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.PlaylistmakerandroidNechaevYaroslavTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.White

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistmakerandroidNechaevYaroslavTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.menu_settings),
            color = Black,
            fontSize = 22.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PlaylistmakerandroidNechaevYaroslavTheme {
        SettingsScreen()
    }
}
