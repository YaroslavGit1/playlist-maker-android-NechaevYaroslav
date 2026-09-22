package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.PlaylistListItem
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistsViewModel
import kotlinx.coroutines.launch

private const val COVER_ICON_FRACTION = 0.4f
private const val ARTWORK_SMALL_SIZE = "100x100"
private const val ARTWORK_LARGE_SIZE = "600x600"
private val CIRCLE_BUTTON_SIZE = 48.dp
private val CIRCLE_ICON_SIZE = 24.dp

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            TrackCover(
                imageUrl = track.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = track.trackName,
                color = colors.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = track.artistName,
                color = colors.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleIconButton(
                    iconRes = R.drawable.ic_playlist_add,
                    contentDescription = stringResource(R.string.add_to_playlist),
                    onClick = { isPlaylistsSheetVisible = true },
                )
                CircleIconButton(
                    iconRes = if (isFavorite) {
                        R.drawable.ic_favorite_filled
                    } else {
                        R.drawable.ic_favorites
                    },
                    contentDescription = stringResource(
                        if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites,
                    ),
                    tint = if (isFavorite) colors.accent else null,
                    onClick = {
                        val newFavorite = !isFavorite
                        isFavorite = newFavorite
                        scope.launch { playlistsViewModel.toggleFavorite(track, newFavorite) }
                    },
                )
            }
            Spacer(Modifier.height(16.dp))
            InfoRow(label = stringResource(R.string.track_duration), value = track.trackTime)
            if (track.album.isNotBlank()) {
                InfoRow(label = stringResource(R.string.track_album), value = track.album)
            }
            if (track.year.isNotBlank()) {
                InfoRow(label = stringResource(R.string.track_year), value = track.year)
            }
            if (track.genre.isNotBlank()) {
                InfoRow(label = stringResource(R.string.track_genre), value = track.genre)
            }
            Spacer(Modifier.height(16.dp))
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
                if (playlists.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_playlists),
                        color = colors.secondary,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                } else {
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
}

@Composable
private fun TrackCover(
    imageUrl: String,
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
        if (imageUrl.isNotBlank()) {
            AsyncImage(
                model = artworkForDetails(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CircleIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    val colors = LocalPlaylistColors.current

    Box(
        modifier = modifier
            .size(CIRCLE_BUTTON_SIZE)
            .clip(CircleShape)
            .background(colors.field)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = tint ?: colors.onField,
            modifier = Modifier.size(CIRCLE_ICON_SIZE),
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = colors.secondary,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            color = colors.onBackground,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun artworkForDetails(url: String): String = url.replace(ARTWORK_SMALL_SIZE, ARTWORK_LARGE_SIZE)
