package com.example.playlist_maker_android_nechaevyaroslav.data.dto

class TracksSearchResponse(
    val tracks: List<TrackDto>,
    resultCode: Int = 200,
) : BaseResponse(resultCode)