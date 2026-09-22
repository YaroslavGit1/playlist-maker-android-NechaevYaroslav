package com.example.playlist_maker_android_nechaevyaroslav.data.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "trackId"],
    indices = [Index("trackId")],
)
data class PlaylistTrackEntity(
    val playlistId: Long,
    val trackId: Long,
)