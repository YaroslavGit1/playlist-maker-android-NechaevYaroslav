package com.example.playlist_maker_android_nechaevyaroslav.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.playlist_maker_android_nechaevyaroslav.R

@Composable
fun tracksCountText(count: Int): String = quantityText(
    count = count,
    oneRes = R.string.tracks_count_one,
    fewRes = R.string.tracks_count_few,
    manyRes = R.string.tracks_count_many,
)

@Composable
fun minutesCountText(minutes: Int): String = quantityText(
    count = minutes,
    oneRes = R.string.minutes_count_one,
    fewRes = R.string.minutes_count_few,
    manyRes = R.string.minutes_count_many,
)

@Composable
private fun quantityText(
    count: Int,
    @StringRes oneRes: Int,
    @StringRes fewRes: Int,
    @StringRes manyRes: Int,
): String {
    val stringResId = when {
        count % 10 == 1 && count % 100 != 11 -> oneRes
        count % 10 in 2..4 && count % 100 !in 12..14 -> fewRes
        else -> manyRes
    }
    return stringResource(stringResId, count)
}
