package com.example.playlist_maker_android_nechaevyaroslav.data.di

import android.content.Context
import androidx.room.Room
import com.example.playlist_maker_android_nechaevyaroslav.data.database.AppDatabase
import org.koin.dsl.module

private const val DATABASE_NAME = "playlists_maker"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}