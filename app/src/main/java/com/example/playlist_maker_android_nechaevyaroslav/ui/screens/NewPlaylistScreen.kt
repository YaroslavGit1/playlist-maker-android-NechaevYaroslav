package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.NewPlaylistViewModel

private const val IMAGE_MIME_TYPE = "image/*"
private val COVER_SIZE = 205.dp
private const val COVER_ICON_FRACTION = 0.4f

@Composable
fun NewPlaylistScreen(
    viewModel: NewPlaylistViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    val colors = LocalPlaylistColors.current
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val coverImageUri by viewModel.coverImageUri.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { viewModel.setCoverImageUri(it.toString()) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch(IMAGE_MIME_TYPE)
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_required),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    val onCoverClick: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            imagePickerLauncher.launch(IMAGE_MIME_TYPE)
        } else if (hasReadStoragePermission(context)) {
            imagePickerLauncher.launch(IMAGE_MIME_TYPE)
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

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
            CoverPicker(
                coverImageUri = coverImageUri,
                onClick = onCoverClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(24.dp))
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
                viewModel.createNewPlaylist(name.trim(), description.trim())
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
private fun CoverPicker(
    coverImageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalPlaylistColors.current

    Box(
        modifier = modifier
            .size(COVER_SIZE)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.field)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cover_photo_add),
            contentDescription = stringResource(R.string.select_image),
            tint = colors.onField,
            modifier = Modifier.fillMaxSize(COVER_ICON_FRACTION),
        )
        if (coverImageUri != null) {
            AsyncImage(
                model = coverImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun hasReadStoragePermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    ) == PackageManager.PERMISSION_GRANTED

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