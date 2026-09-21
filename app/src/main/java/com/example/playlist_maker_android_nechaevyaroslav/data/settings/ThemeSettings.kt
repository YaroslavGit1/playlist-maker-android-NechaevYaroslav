package com.example.playlist_maker_android_nechaevyaroslav.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSettings(private val context: Context) {

    val isDarkTheme: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[IS_DARK_THEME_KEY] ?: false
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = enabled
        }
    }
}
