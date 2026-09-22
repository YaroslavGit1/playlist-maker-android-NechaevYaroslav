package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.database.AppDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistTrackEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.toEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.toTrack
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TrackDto
import com.example.playlist_maker_android_nechaevyaroslav.data.network.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.network.NetworkResult
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchResult
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    database: AppDatabase,
) : TracksRepository {

    private val tracksDao = database.tracksDao()

    override suspend fun searchTracks(expression: String): SearchResult =
        when (val result = networkClient.searchTracks(expression)) {
            is NetworkResult.Success -> SearchResult.Success(result.data.map { it.toTrack() })
            is NetworkResult.ServerError -> SearchResult.ServerError
            is NetworkResult.NetworkError -> SearchResult.NetworkError
        }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        tracksDao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { it?.toTrack() }

    override fun getFavoriteTracks(): Flow<List<Track>> =
        tracksDao.getFavoriteTracks().map { tracks -> tracks.map { it.toTrack() } }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        tracksDao.insertTrackIfAbsent(track.toEntity())
        tracksDao.insertPlaylistTrack(PlaylistTrackEntity(playlistId, track.id))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        tracksDao.deletePlaylistTrack(playlistId, track.id)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        tracksDao.insertTrack(track.copy(favorite = isFavorite).toEntity())
    }
}

private fun TrackDto.toTrack(): Track {
    val durationMillis = trackTimeMillis ?: 0L
    return Track(
        id = trackId ?: 0L,
        trackName = trackName.orEmpty(),
        artistName = artistName.orEmpty(),
        trackTime = formatDuration(durationMillis),
        trackDurationMillis = durationMillis,
        image = artworkUrl100.orEmpty(),
    )
}

private fun formatDuration(durationMillis: Long): String {
    val totalSeconds = durationMillis.coerceAtLeast(0L) / 1000
    return String.format(Locale.ROOT, "%d:%02d", totalSeconds / 60, totalSeconds % 60)
}