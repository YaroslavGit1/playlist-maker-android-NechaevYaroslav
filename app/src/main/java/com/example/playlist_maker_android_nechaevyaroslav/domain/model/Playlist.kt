package com.example.playlist_maker_android_nechaevyaroslav.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImageUri: String? = null,
    val tracks: List<Track>,
)
