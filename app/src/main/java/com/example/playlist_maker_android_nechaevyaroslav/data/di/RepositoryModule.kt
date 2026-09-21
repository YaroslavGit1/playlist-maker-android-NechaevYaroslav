package com.example.playlist_maker_android_nechaevyaroslav.data.di

import com.example.playlist_maker_android_nechaevyaroslav.data.DatabaseMock
import com.example.playlist_maker_android_nechaevyaroslav.data.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.SearchHistoryRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.Storage
import com.example.playlist_maker_android_nechaevyaroslav.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.network.TracksRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        Storage()
    }
    single {
        DatabaseMock()
    }
    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
}