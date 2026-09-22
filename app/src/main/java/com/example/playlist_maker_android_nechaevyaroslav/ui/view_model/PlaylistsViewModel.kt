package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
) : ViewModel() {

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    val favoriteList: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        tracksRepository.insertTrackToPlaylist(track, playlistId)
    }

    suspend fun removeTrackFromPlaylist(track: Track, playlistId: Long) {
        tracksRepository.deleteTrackFromPlaylist(track, playlistId)
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
    }

    suspend fun deletePlaylistById(id: Long) {
        playlistsRepository.deletePlaylistById(id)
    }

    suspend fun isExist(track: Track): Track? = tracksRepository.getTrackByNameAndArtist(track).firstOrNull()
}
