package com.example.playlist_maker_android_nechaevyaroslav.domain.api

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun searchTracks(expression: String): SearchResult

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
}
