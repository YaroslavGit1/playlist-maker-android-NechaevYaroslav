package com.example.playlist_maker_android_nechaevyaroslav.data.database.entity

import androidx.room.Entity

@Entity(tableName = "playlist_tracks", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackEntity(
    val playlistId: Long,
    val trackId: Long,
)