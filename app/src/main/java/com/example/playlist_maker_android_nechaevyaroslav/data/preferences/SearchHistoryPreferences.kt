package com.example.playlist_maker_android_nechaevyaroslav.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal val Context.searchHistoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")

private const val MAX_ENTRIES = 10
private const val SEPARATOR = ","

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(
        CoroutineName("search-history-preferences") + SupervisorJob() + Dispatchers.IO,
    ),
) {

    fun addEntry(word: String) {
        if (word.isEmpty()) {
            return
        }

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[SEARCH_HISTORY_KEY].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(word)
                history.add(0, word)

                preferences[SEARCH_HISTORY_KEY] = history.take(MAX_ENTRIES).joinToString(SEPARATOR)
            }
        }
    }

    suspend fun getEntries(): List<String> = dataStore.data
        .map { preferences ->
            preferences[SEARCH_HISTORY_KEY]
                .orEmpty()
                .split(SEPARATOR)
                .filter { it.isNotEmpty() }
        }
        .first()
}