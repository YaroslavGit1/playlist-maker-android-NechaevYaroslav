package com.example.playlist_maker_android_nechaevyaroslav.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.AppDatabase
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.PlaylistTrackEntity
import com.example.playlist_maker_android_nechaevyaroslav.data.database.entity.TrackEntity
import org.koin.dsl.module

private const val DATABASE_NAME = "playlists_maker"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .addCallback(InitialDataCallback)
            .build()
    }
}

// Начальное наполнение базы: треки плейлистов 1 и 2 создаются вместе с базой.
private object InitialDataCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        initialTracks.forEach { track ->
            db.execSQL(
                "INSERT INTO tracks (id, trackName, artistName, trackTime, " +
                    "trackDurationMillis, image, favorite) VALUES (?, ?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(
                    track.id,
                    track.trackName,
                    track.artistName,
                    track.trackTime,
                    track.trackDurationMillis,
                    track.image,
                    if (track.favorite) 1 else 0,
                ),
            )
        }
        initialPlaylistTracks.forEach { link ->
            db.execSQL(
                "INSERT INTO playlist_tracks (playlistId, trackId) VALUES (?, ?)",
                arrayOf<Any?>(link.playlistId, link.trackId),
            )
        }
    }

    private val initialTracks = listOf(
        TrackEntity(1, "Yesterday (Remastered 2009)", "The Beatles", "2:55", 175_000L, ""),
        TrackEntity(2, "Here Comes The Sun (Remastered 2009)", "The Beatles", "4:01", 241_000L, ""),
        TrackEntity(3, "No Reply", "The Beatles", "5:12", 312_000L, ""),
        TrackEntity(4, "Let It Be", "The Beatles", "6:01", 361_000L, ""),
        TrackEntity(5, "Girl", "The Beatles", "4:11", 251_000L, ""),
        TrackEntity(6, "Michelle", "The Beatles", "3:01", 181_000L, ""),
        TrackEntity(7, "Eleanor Rigby", "The Beatles", "6:12", 372_000L, ""),
        TrackEntity(8, "Come Together", "The Beatles", "4:09", 249_000L, ""),
    )

    private val initialPlaylistTracks = listOf(
        PlaylistTrackEntity(1, 1),
        PlaylistTrackEntity(1, 2),
        PlaylistTrackEntity(1, 3),
        PlaylistTrackEntity(1, 4),
        PlaylistTrackEntity(1, 5),
        PlaylistTrackEntity(2, 6),
        PlaylistTrackEntity(2, 7),
        PlaylistTrackEntity(2, 8),
    )
}