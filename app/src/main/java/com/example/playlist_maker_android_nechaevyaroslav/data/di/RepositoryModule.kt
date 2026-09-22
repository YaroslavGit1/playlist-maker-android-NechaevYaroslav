package com.example.playlist_maker_android_nechaevyaroslav.data.di

import com.example.playlist_maker_android_nechaevyaroslav.data.DatabaseMock
import com.example.playlist_maker_android_nechaevyaroslav.data.PlaylistsRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.SearchHistoryRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.TracksRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.network.ITunesApi
import com.example.playlist_maker_android_nechaevyaroslav.data.network.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_BASE_URL = "https://itunes.apple.com/"

val repositoryModule = module {
    single {
        DatabaseMock()
    }
    single {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ITunesApi> {
        get<Retrofit>().create(ITunesApi::class.java)
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    factory<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }
    factory<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get())
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
}
