package com.example.playlist_maker_android_nechaevyaroslav.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackDurationMillis: Long,
    val image: String,
    val favorite: Boolean = false,
)

fun TrackEntity.toTrack(): Track = Track(
    id = id,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    trackDurationMillis = trackDurationMillis,
    image = image,
    favorite = favorite,
)

fun Track.toEntity(): TrackEntity = TrackEntity(
    id = id,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    trackDurationMillis = trackDurationMillis,
    image = image,
    favorite = favorite,
)