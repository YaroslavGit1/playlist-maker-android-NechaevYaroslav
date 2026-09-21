package com.example.playlist_maker_android_nechaevyaroslav.data

class DatabaseMock {

    private val historyList = mutableListOf<String>()

    fun getHistory(): List<String> = historyList.toList()

    fun addToHistory(word: String) {
        historyList.add(word)
    }
}
