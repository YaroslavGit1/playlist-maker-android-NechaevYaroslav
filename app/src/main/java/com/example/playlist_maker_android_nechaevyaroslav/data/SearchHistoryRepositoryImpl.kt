package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.preferences.SearchHistoryPreferences
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val searchHistoryPreferences: SearchHistoryPreferences,
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<String> = searchHistoryPreferences.getEntries()

    override fun addToHistory(word: String) {
        searchHistoryPreferences.addEntry(word)
    }
}