package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Playlist
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseMock {

    private val historyList = mutableListOf<String>()
    private val playlists = mutableListOf<Playlist>()
    private val playlistIdCounter = AtomicLong(0)
    private val trackIdCounter = AtomicLong(0)
    private val tracks = mutableListOf<Track>()
    private val playlistTrackRefs = mutableListOf<Pair<Long, Long>>()

    fun getHistory(): List<String> = historyList.toList()

    fun addToHistory(word: String) {
        historyList.add(word)
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(PLAYLISTS_DELAY_MILLIS)
        emit(playlists.map { playlist -> playlist.copy(tracks = tracksOfPlaylist(playlist.id)) })
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        val playlist = playlists.find { it.id == id }
        emit(playlist?.copy(tracks = tracksOfPlaylist(id)))
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
        playlistTrackRefs.removeIf { it.first == playlistId }
    }

    fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        playlistTrackRefs.add(playlistId to trackId)
    }

    fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) {
        playlistTrackRefs.remove(playlistId to trackId)
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        val index = tracks.indexOfFirst { it.id == track.id }
        if (index >= 0) {
            tracks[index] = track
        } else {
            tracks.add(track)
        }
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(FAVORITES_DELAY_MILLIS)
        emit(tracks.filter { it.favorite })
    }

    suspend fun searchTracks(expression: String): List<Track> {
        delay(SEARCH_DELAY_MILLIS)
        return tracks.filter {
            it.trackName.contains(expression, ignoreCase = true) ||
                it.artistName.contains(expression, ignoreCase = true)
        }
    }

    private fun tracksOfPlaylist(playlistId: Long): List<Track> {
        val trackIds = playlistTrackRefs
            .filter { it.first == playlistId }
            .map { it.second }
        return tracks.filter { it.id in trackIds }
    }

    init {
        tracks.addAll(
            listOf(
                Track(trackIdCounter.incrementAndGet(), "Yesterday (Remastered 2009)", "The Beatles", "2:55", 175_000L),
                Track(trackIdCounter.incrementAndGet(), "Here Comes The Sun (Remastered 2009)", "The Beatles", "4:01", 241_000L),
                Track(trackIdCounter.incrementAndGet(), "No Reply", "The Beatles", "5:12", 312_000L),
                Track(trackIdCounter.incrementAndGet(), "Let It Be", "The Beatles", "6:01", 361_000L),
                Track(trackIdCounter.incrementAndGet(), "Girl", "The Beatles", "4:11", 251_000L),
                Track(trackIdCounter.incrementAndGet(), "Michelle", "The Beatles", "3:01", 181_000L),
                Track(trackIdCounter.incrementAndGet(), "Eleanor Rigby", "The Beatles", "6:12", 372_000L),
                Track(trackIdCounter.incrementAndGet(), "Come Together", "The Beatles", "4:09", 249_000L),
            ),
        )
        playlists.addAll(
            listOf(
                Playlist(playlistIdCounter.incrementAndGet(), "Best songs 2021", "My favorite tracks", null, emptyList()),
                Playlist(playlistIdCounter.incrementAndGet(), "Summer Party", "Beach vibes", null, emptyList()),
            ),
        )
        playlistTrackRefs.addAll(
            listOf(
                1L to 1L,
                1L to 2L,
                1L to 3L,
                1L to 4L,
                1L to 5L,
                2L to 6L,
                2L to 7L,
                2L to 8L,
            ),
        )
        historyList.addAll(listOf("Rammstein", "Queen", "Bruno Mars"))
    }

    private companion object {
        const val SEARCH_DELAY_MILLIS = 3000L
        const val PLAYLISTS_DELAY_MILLIS = 500L
        const val FAVORITES_DELAY_MILLIS = 300L
    }
}
