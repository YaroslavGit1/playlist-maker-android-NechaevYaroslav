package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.SearchState
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    navigateToDetailScreen: (Track) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenState by searchViewModel.searchScreenState.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val colors = LocalPlaylistColors.current

    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        ScreenHeader(
            title = stringResource(R.string.menu_search),
            onBackClick = navigateBack,
        )
        SearchField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                if (searchQuery.isNotEmpty()) {
                    searchViewModel.searchTracks(searchQuery)
                    focusManager.clearFocus()
                }
            },
            onClear = {
                searchQuery = ""
                searchViewModel.clearSearch()
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        SearchResults(
            screenState = screenState,
            onTrackClick = navigateToDetailScreen,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.field),
        textStyle = TextStyle(color = colors.onField, fontSize = 16.sp),
        singleLine = true,
        cursorBrush = SolidColor(colors.accent),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search_icon),
                    tint = colors.onField,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onSearch),
                )
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_tracks_placeholder),
                            color = colors.onField,
                            fontSize = 16.sp,
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.clear_search),
                        tint = colors.onField,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onClear),
                    )
                }
            }
        },
    )
}

@Composable
private fun SearchResults(
    screenState: SearchState,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (screenState) {
        is SearchState.Initial -> {
        }

        is SearchState.Searching -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchState.Success -> {
            val tracks = screenState.foundList
            if (tracks.isEmpty()) {
                InformerState(titleText = stringResource(R.string.no_tracks_found))
            } else {
                LazyColumn(modifier = modifier) {
                    items(tracks) { track ->
                        TrackListItem(
                            track = track,
                            onClick = { onTrackClick(track) },
                        )
                    }
                }
            }
        }

        is SearchState.Fail -> {
            InformerState(
                titleText = screenState.error,
                subtitleText = stringResource(R.string.check_connection),
            )
        }
    }
}

@Composable
private fun InformerState(
    titleText: String,
    subtitleText: String? = null,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = titleText,
            color = colors.onBackground,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
        subtitleText?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                color = colors.secondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TrackListItem(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = null,
            tint = colors.secondary,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.trackName,
                color = colors.onBackground,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    color = colors.secondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Box(
                    modifier = Modifier.size(13.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(colors.secondary),
                    )
                }
                Text(
                    text = track.trackTime,
                    color = colors.secondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = stringResource(R.string.arrow_forward),
            tint = colors.secondary,
            modifier = Modifier.size(24.dp),
        )
    }
}
