package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
    private val playlistId: Long,
) : ViewModel() {

    val playlist: Flow<Playlist?> = playlistsRepository.getPlaylist(playlistId)

    fun removeTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track, playlistId)
        }
    }
}
