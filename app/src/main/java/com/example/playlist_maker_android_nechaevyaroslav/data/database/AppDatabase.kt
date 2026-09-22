package com.example.playlist_maker_android_nechaevyaroslav.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.dao.PlaylistsDao
import com.example.playlist_maker_android_nechaevyaroslav.data.database.dao.TracksDao
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistTrackEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao

    abstract fun playlistsDao(): PlaylistsDao
}