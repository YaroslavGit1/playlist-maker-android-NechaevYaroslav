package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.database.AppDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.toTrack
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PlaylistsRepositoryImpl(
    private val database: DatabaseMock,
    appDatabase: AppDatabase,
) : PlaylistsRepository {

    private val tracksDao = appDatabase.tracksDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> = combine(
        database.getPlaylist(playlistId),
        tracksDao.getTracksFromPlaylist(playlistId),
    ) { playlist, tracks ->
        playlist?.copy(tracks = tracks.map { it.toTrack() })
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = combine(
        database.getAllPlaylists(),
        tracksDao.getAllTracks(),
        tracksDao.getPlaylistTracks(),
    ) { playlists, tracks, playlistTracks ->
        playlists.map { playlist ->
            val trackIds = playlistTracks
                .filter { it.playlistId == playlist.id }
                .map { it.trackId }
                .toSet()
            playlist.copy(tracks = tracks.filter { it.id in trackIds }.map { it.toTrack() })
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description, null)
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
        tracksDao.deletePlaylistTracks(id)
    }
}