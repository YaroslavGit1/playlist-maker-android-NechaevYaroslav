package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.TrackListItem
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.minutesCountText
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.tracksCountText
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistViewModel

private const val MILLIS_PER_MINUTE = 60_000L

@Composable
fun PlaylistScreen(
    playlistViewModel: PlaylistViewModel,
    onTrackClick: (Track) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    val colors = LocalPlaylistColors.current
    val playlist by playlistViewModel.playlist.collectAsState(initial = null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        ScreenHeader(onBackClick = onBackClick)
        playlist?.let { data ->
            PlaylistContent(
                playlist = data,
                onTrackClick = onTrackClick,
                onTrackRemove = playlistViewModel::removeTrackFromPlaylist,
            )
        }
    }
}

@Composable
private fun PlaylistContent(
    playlist: Playlist,
    onTrackClick: (Track) -> Unit,
    onTrackRemove: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current
    var trackToRemove by remember { mutableStateOf<Track?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PlaylistCover(
                    coverImageUri = playlist.coverImageUri,
                    playlistName = playlist.name,
                    modifier = Modifier
                        .fillMaxWidth(COVER_WIDTH_FRACTION)
                        .aspectRatio(1f),
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = playlist.name,
                    color = colors.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                if (playlist.description.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = playlist.description,
                        color = colors.onBackground,
                        fontSize = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = playlistStats(playlist),
                    color = colors.secondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(16.dp))
            }
        }
        items(playlist.tracks) { track ->
            TrackListItem(
                track = track,
                onClick = { onTrackClick(track) },
                onLongClick = { trackToRemove = track },
            )
        }
    }

    trackToRemove?.let { track ->
        AlertDialog(
            onDismissRequest = { trackToRemove = null },
            title = { Text(text = stringResource(R.string.remove_track_title)) },
            text = {
                Text(text = stringResource(R.string.remove_track_message, track.trackName))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTrackRemove(track)
                        trackToRemove = null
                    },
                ) {
                    Text(text = stringResource(R.string.remove_track_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { trackToRemove = null }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun PlaylistCover(
    coverImageUri: String?,
    playlistName: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.field),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_music),
            contentDescription = null,
            tint = colors.onField,
            modifier = Modifier.fillMaxSize(COVER_ICON_FRACTION),
        )
        if (!coverImageUri.isNullOrBlank()) {
            AsyncImage(
                model = coverImageUri,
                contentDescription = stringResource(R.string.playlist_cover, playlistName),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun playlistStats(playlist: Playlist): String {
    val totalMinutes = playlist.tracks.sumOf { it.trackDurationMillis } / MILLIS_PER_MINUTE
    return stringResource(
        R.string.playlist_stats,
        minutesCountText(totalMinutes.toInt()),
        tracksCountText(playlist.tracks.size),
    )
}

private const val COVER_WIDTH_FRACTION = 0.8f
private const val COVER_ICON_FRACTION = 0.4f
