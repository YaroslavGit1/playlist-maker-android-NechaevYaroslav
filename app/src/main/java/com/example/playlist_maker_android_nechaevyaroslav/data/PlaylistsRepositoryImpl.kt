package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.database.AppDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.toPlaylist
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    database: AppDatabase,
) : PlaylistsRepository {

    private val playlistsDao = database.playlistsDao()
    private val tracksDao = database.tracksDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> =
        playlistsDao.getPlaylistWithTracks(playlistId).map { it?.toPlaylist() }

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistsDao.getPlaylistsWithTracks().map { playlists -> playlists.map { it.toPlaylist() } }

    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        playlistsDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                coverImageUri = coverImageUri,
            ),
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistsDao.deletePlaylistById(id)
        tracksDao.deletePlaylistTracks(id)
    }
}