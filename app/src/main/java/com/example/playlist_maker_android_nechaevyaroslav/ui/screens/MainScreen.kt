package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.Black
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.Blue
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.Gray
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.PlaylistmakerandroidNechaevYaroslavTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.White

private data class MainMenuItem(
    val iconRes: Int,
    val titleRes: Int,
    val onClick: () -> Unit,
)

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(lightIcons = true)

    val menuItems = listOf(
        MainMenuItem(R.drawable.ic_search, R.string.menu_search, onSearchClick),
        MainMenuItem(R.drawable.ic_playlists, R.string.menu_playlists, {}),
        MainMenuItem(R.drawable.ic_favorites, R.string.menu_favorites, {}),
        MainMenuItem(R.drawable.ic_settings, R.string.menu_settings, onSettingsClick),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Blue),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(White)
                .padding(top = 8.dp),
        ) {
            menuItems.forEach { item ->
                MainMenuItemRow(
                    item = item,
                    onClick = item.onClick,
                )
            }
        }
    }
}

@Composable
private fun MainMenuItemRow(
    item: MainMenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            tint = Black,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(item.titleRes),
            color = Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = Gray,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    PlaylistmakerandroidNechaevYaroslavTheme {
        MainScreen(onSearchClick = {}, onSettingsClick = {})
    }
}
