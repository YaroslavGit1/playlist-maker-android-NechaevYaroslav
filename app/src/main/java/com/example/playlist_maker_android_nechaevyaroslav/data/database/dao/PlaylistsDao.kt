package com.example.playlist_maker_android_nechaevyaroslav.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks?>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)
}