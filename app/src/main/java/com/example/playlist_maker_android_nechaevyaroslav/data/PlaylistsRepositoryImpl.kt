package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val database: DatabaseMock,
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> = database.getPlaylist(playlistId)

    override fun getAllPlaylists(): Flow<List<Playlist>> = database.getAllPlaylists()

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description, null)
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
    }
}
