package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.PlaylistListItem
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    val colors = LocalPlaylistColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isFavorite by remember { mutableStateOf(false) }
    var isPlaylistsSheetVisible by remember { mutableStateOf(false) }
    val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())

    LaunchedEffect(track) {
        isFavorite = playlistsViewModel.isExist(track)?.favorite ?: false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        ScreenHeader(onBackClick = onBackClick)
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = track.trackName,
                color = colors.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = track.artistName,
                color = colors.secondary,
                fontSize = 16.sp,
            )
            Text(
                text = track.trackTime,
                color = colors.secondary,
                fontSize = 16.sp,
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    val newFavorite = !isFavorite
                    isFavorite = newFavorite
                    scope.launch { playlistsViewModel.toggleFavorite(track, newFavorite) }
                },
            ) {
                Icon(
                    painter = painterResource(
                        if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorites,
                    ),
                    contentDescription = stringResource(
                        if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites,
                    ),
                    tint = if (isFavorite) colors.accent else colors.onBackground,
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { isPlaylistsSheetVisible = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_playlist_add),
                    contentDescription = stringResource(R.string.add_to_playlist),
                    tint = colors.onBackground,
                )
            }
        }
    }

    if (isPlaylistsSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isPlaylistsSheetVisible = false },
            containerColor = colors.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            ) {
                Text(
                    text = stringResource(R.string.add_to_playlist),
                    color = colors.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
                playlists.forEach { playlist ->
                    PlaylistListItem(
                        playlist = playlist,
                        onClick = {
                            isPlaylistsSheetVisible = false
                            scope.launch {
                                playlistsViewModel.addTrackToPlaylist(track, playlist.id)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.added_to_playlist, playlist.name),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        },
                    )
                }
            }
        }
    }
}
