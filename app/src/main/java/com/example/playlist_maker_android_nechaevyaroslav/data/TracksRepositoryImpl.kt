package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(
    private val database: DatabaseMock,
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> = database.searchTracks(expression)

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> = database.getTrackByNameAndArtist(track)

    override fun getFavoriteTracks(): Flow<List<Track>> = database.getFavoriteTracks()

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.addTrackToPlaylist(track.id, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        database.removeTrackFromPlaylist(track.id, playlistId)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }
}
