package com.example.playlist_maker_android_nechaevyaroslav.domain.api

interface SearchHistoryRepository {

    suspend fun getHistory(): List<String>

    fun addToHistory(word: String)
}
