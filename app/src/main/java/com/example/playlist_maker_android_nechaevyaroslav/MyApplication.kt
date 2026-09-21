package com.example.playlist_maker_android_nechaevyaroslav

import android.app.Application
import com.example.playlist_maker_android_nechaevyaroslav.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.network.TracksRepositoryImpl
import com.example.playlist_maker_android_nechaevyaroslav.data.settings.ThemeSettings
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository

class MyApplication : Application() {

    fun getTracksRepository(): TracksRepository = TracksRepositoryImpl(RetrofitNetworkClient())

    fun provideThemeSettings(): ThemeSettings = ThemeSettings(this)
}
