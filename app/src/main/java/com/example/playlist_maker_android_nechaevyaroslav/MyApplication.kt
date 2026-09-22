package com.example.playlist_maker_android_nechaevyaroslav

import android.app.Application
import com.example.playlist_maker_android_nechaevyaroslav.data.di.databaseModule
import com.example.playlist_maker_android_nechaevyaroslav.data.di.repositoryModule
import com.example.playlist_maker_android_nechaevyaroslav.data.settings.ThemeSettings
import com.example.playlist_maker_android_nechaevyaroslav.ui.view_model.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(databaseModule, repositoryModule, viewModelModule)
        }
    }

    fun provideThemeSettings(): ThemeSettings = ThemeSettings(this)
}