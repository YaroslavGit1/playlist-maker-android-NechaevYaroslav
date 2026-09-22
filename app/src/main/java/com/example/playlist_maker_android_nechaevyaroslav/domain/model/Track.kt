package com.example.playlist_maker_android_nechaevyaroslav.domain.model

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackDurationMillis: Long = 0L,
    val image: String = "",
    val favorite: Boolean = false,
)
