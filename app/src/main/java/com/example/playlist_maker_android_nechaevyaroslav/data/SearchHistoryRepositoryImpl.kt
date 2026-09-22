package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val database: DatabaseMock,
) : SearchHistoryRepository {

    override suspend fun getHistory(): List<String> = database.getHistory()

    override fun addToHistory(word: String) {
        database.addToHistory(word)
    }
}
