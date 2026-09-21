package com.example.playlist_maker_android_nechaevyaroslav

import android.app.Application
import com.example.playlist_maker_android_nechaevyaroslav.data.di.repositoryModule
import com.example.playlist_maker_android_nechaevyaroslav.data.settings.ThemeSettings
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.viewModelModule
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(repositoryModule, viewModelModule)
        }
    }

    fun provideThemeSettings(): ThemeSettings = ThemeSettings(this)
}