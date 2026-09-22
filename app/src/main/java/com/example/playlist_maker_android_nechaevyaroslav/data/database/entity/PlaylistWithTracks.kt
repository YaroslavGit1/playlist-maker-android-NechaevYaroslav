package com.example.playlist_maker_android_nechaevyaroslav.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist

data class PlaylistWithTracks(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlaylistTrackEntity::class,
            parentColumn = "playlistId",
            entityColumn = "trackId",
        ),
    )
    val tracks: List<TrackEntity>,
)

fun PlaylistWithTracks.toPlaylist(): Playlist = Playlist(
    id = playlist.id,
    name = playlist.name,
    description = playlist.description,
    coverImageUri = playlist.coverImageUri,
    tracks = tracks.map { it.toTrack() },
)