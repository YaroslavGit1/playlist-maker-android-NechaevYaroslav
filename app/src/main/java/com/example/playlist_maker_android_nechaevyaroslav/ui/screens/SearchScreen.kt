package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.TrackListItem
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
    var history by remember { mutableStateOf<List<String>>(emptyList()) }
    var isSearchFieldFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = LocalPlaylistColors.current

    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    LaunchedEffect(screenState) {
        val isResultShown = screenState is SearchState.Success ||
            screenState is SearchState.ServerError ||
            screenState is SearchState.NetworkError
        if (isResultShown) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(isSearchFieldFocused, searchQuery) {
        if (isSearchFieldFocused && searchQuery.isEmpty()) {
            history = searchViewModel.getHistoryList()
        }
    }

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
            onQueryChange = {
                searchQuery = it
                searchViewModel.updateQuery(it)
            },
            onSearch = focusManager::clearFocus,
            onClear = {
                searchQuery = ""
                searchViewModel.clearSearch()
                focusRequester.requestFocus()
                keyboardController?.hide()
            },
            focusRequester = focusRequester,
            onFocusChanged = { isSearchFieldFocused = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        if (isSearchFieldFocused && searchQuery.isEmpty() && history.isNotEmpty()) {
            SearchHistory(
                history = history,
                onHistoryClick = { word ->
                    searchQuery = word
                    searchViewModel.updateQuery(word)
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        if (searchQuery.isNotEmpty()) {
            SearchResults(
                screenState = screenState,
                onTrackClick = navigateToDetailScreen,
                onRetry = searchViewModel::retryLastSearch,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .clip(RoundedCornerShape(8.dp))
            .background(colors.field),
        textStyle = TextStyle(color = colors.onField, fontSize = 16.sp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
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
                    modifier = Modifier.size(16.dp),
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
private fun SearchHistory(
    history: List<String>,
    onHistoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
    ) {
        items(history) { word ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onHistoryClick(word) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_history),
                    contentDescription = null,
                    tint = colors.secondary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = word,
                    color = colors.onBackground,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SearchResults(
    screenState: SearchState,
    onTrackClick: (Track) -> Unit,
    onRetry: () -> Unit,
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
                InformerState(
                    imageRes = R.drawable.ic_nothing_found,
                    titleText = stringResource(R.string.no_tracks_found),
                )
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

        is SearchState.ServerError -> {
            InformerState(
                imageRes = R.drawable.ic_server_error,
                titleText = stringResource(R.string.server_error),
                actionText = stringResource(R.string.retry_search),
                onAction = onRetry,
            )
        }

        is SearchState.NetworkError -> {
            InformerState(
                imageRes = R.drawable.ic_server_error,
                titleText = stringResource(R.string.search_error),
                subtitleText = stringResource(R.string.check_connection),
                actionText = stringResource(R.string.retry_search),
                onAction = onRetry,
            )
        }
    }
}

@Composable
private fun InformerState(
    @DrawableRes imageRes: Int,
    titleText: String,
    modifier: Modifier = Modifier,
    subtitleText: String? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = LocalPlaylistColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(imageRes),
            contentDescription = null,
            tint = colors.onBackground,
            modifier = Modifier.size(120.dp),
        )
        Spacer(Modifier.height(16.dp))
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
        if (actionText != null && onAction != null) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.onAccent,
                ),
            ) {
                Text(
                    text = actionText,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
