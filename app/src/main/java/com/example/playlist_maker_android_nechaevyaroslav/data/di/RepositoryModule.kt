package com.example.playlist_maker_android_nechaevyaroslav.data.di

import com.example.playlist_maker_android_nechaevyaroslav.data.DatabaseMock
import com.example.playlist_maker_android_nechaevyaroslav.data.PlaylistsRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.SearchHistoryRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.TracksRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        DatabaseMock()
    }
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    factory<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get())
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
}
