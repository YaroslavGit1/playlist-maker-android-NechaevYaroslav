package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistViewModel(
    playlistsRepository: PlaylistsRepository,
    playlistId: Long,
) : ViewModel() {

    val playlist: Flow<Playlist?> = playlistsRepository.getPlaylist(playlistId)
}
