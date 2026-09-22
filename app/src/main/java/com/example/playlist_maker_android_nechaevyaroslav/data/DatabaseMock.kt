package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseMock {

    private val historyList = mutableListOf<String>()
    private val playlists = mutableListOf<Playlist>()
    private val playlistIdCounter = AtomicLong(0)

    fun getHistory(): List<String> = historyList.toList()

    fun addToHistory(word: String) {
        historyList.add(word)
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(PLAYLISTS_DELAY_MILLIS)
        emit(playlists.toList())
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == id })
    }

    fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        playlists.add(
            Playlist(
                id = playlistIdCounter.incrementAndGet(),
                name = name,
                description = description,
                coverImageUri = coverImageUri,
                tracks = emptyList(),
            ),
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    init {
        playlists.addAll(
            listOf(
                Playlist(playlistIdCounter.incrementAndGet(), "Best songs 2021", "My favorite tracks", null, emptyList()),
                Playlist(playlistIdCounter.incrementAndGet(), "Summer Party", "Beach vibes", null, emptyList()),
            ),
        )
        historyList.addAll(listOf("Rammstein", "Queen", "Bruno Mars"))
    }

    private companion object {
        const val PLAYLISTS_DELAY_MILLIS = 500L
    }
}