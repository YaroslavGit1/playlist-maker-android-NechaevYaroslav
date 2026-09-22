package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TrackDto
import com.example.playlist_maker_android_nechaevyaroslav.data.network.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.network.NetworkResult
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchResult
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import java.util.Locale
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(
    private val database: DatabaseMock,
    private val networkClient: NetworkClient,
) : TracksRepository {

    override suspend fun searchTracks(expression: String): SearchResult =
        when (val result = networkClient.searchTracks(expression)) {
            is NetworkResult.Success -> SearchResult.Success(result.data.map { it.toTrack() })
            is NetworkResult.ServerError -> SearchResult.ServerError
            is NetworkResult.NetworkError -> SearchResult.NetworkError
        }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> = database.getTrackByNameAndArtist(track)

    override fun getFavoriteTracks(): Flow<List<Track>> = database.getFavoriteTracks()

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track)
        database.addTrackToPlaylist(track.id, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Long) {
        database.removeTrackFromPlaylist(track.id, playlistId)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
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
