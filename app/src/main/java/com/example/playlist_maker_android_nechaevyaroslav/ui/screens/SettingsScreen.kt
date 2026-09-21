package com.example.playlist_maker_android_nechaevyaroslav.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_nechaevyaroslav.R
import com.example.playlist_maker_android_nechaevyaroslav.ui.components.ScreenHeader
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalDarkTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.LocalPlaylistColors
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.PlaylistmakerandroidNechaevYaroslavTheme
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.StatusBarIcons
import com.example.playlist_maker_android_nechaevyaroslav.ui.theme.White

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    darkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.share_message)
    val developerEmail = stringResource(R.string.developer_email)
    val emailSubject = stringResource(R.string.email_subject)
    val emailBody = stringResource(R.string.email_body)
    val offerUrl = stringResource(R.string.offer_url)

    val colors = LocalPlaylistColors.current

    StatusBarIcons(lightIcons = LocalDarkTheme.current)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        ScreenHeader(
            title = stringResource(R.string.menu_settings),
            onBackClick = onBackClick,
        )
        Spacer(Modifier.height(24.dp))
        SettingsRow(
            title = stringResource(R.string.settings_dark_theme),
            trailing = {
                Switch(
                    checked = darkThemeEnabled,
                    onCheckedChange = onDarkThemeChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        checkedTrackColor = colors.accent,
                    ),
                )
            },
        )
        SettingsRow(
            title = stringResource(R.string.settings_share),
            iconRes = R.drawable.ic_share,
            onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                context.openIntent(Intent.createChooser(sendIntent, null))
            },
        )
        SettingsRow(
            title = stringResource(R.string.settings_support),
            iconRes = R.drawable.ic_support,
            onClick = {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
                    putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }
                context.openIntent(emailIntent)
            },
        )
        SettingsRow(
            title = stringResource(R.string.settings_agreement),
            iconRes = R.drawable.ic_arrow_forward,
            onClick = {
                context.openIntent(Intent(Intent.ACTION_VIEW, Uri.parse(offerUrl)))
            },
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = LocalPlaylistColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = colors.onBackground,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        if (trailing != null) {
            trailing()
        } else if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = colors.secondary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

private fun Context.openIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        // на устройстве нет приложения, которое умеет обрабатывать такое действие
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PlaylistmakerandroidNechaevYaroslavTheme {
        SettingsScreen(onBackClick = {}, darkThemeEnabled = false, onDarkThemeChange = {})
    }
}
