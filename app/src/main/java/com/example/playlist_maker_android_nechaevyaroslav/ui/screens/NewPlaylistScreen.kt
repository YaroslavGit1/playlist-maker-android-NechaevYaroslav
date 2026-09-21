package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.PlaylistsViewModel

@Composable
fun NewPlaylistScreen(
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    val colors = LocalPlaylistColors.current
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .imePadding(),
    ) {
        ScreenHeader(
            title = stringResource(R.string.new_playlist_title),
            onBackClick = onBackClick,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            PlaylistTextField(
                value = name,
                onValueChange = { name = it },
                hint = stringResource(R.string.playlist_name_hint),
            )
            Spacer(Modifier.height(16.dp))
            PlaylistTextField(
                value = description,
                onValueChange = { description = it },
                hint = stringResource(R.string.playlist_description_hint),
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                playlistsViewModel.createNewPlaylist(name.trim(), description.trim())
                onBackClick()
            },
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.onAccent,
            ),
        ) {
            Text(
                text = stringResource(R.string.playlist_save),
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
private fun PlaylistTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = hint,
                color = colors.onField,
                fontSize = 16.sp,
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.secondary,
            focusedTextColor = colors.onBackground,
            unfocusedTextColor = colors.onBackground,
            cursorColor = colors.accent,
        ),
    )
}
