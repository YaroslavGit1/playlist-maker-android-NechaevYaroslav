package com.example.playlist_maker_android_nechaevyaroslav.domain.api

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track

interface TracksRepository {
    suspend fun searchTracksContains(expression: String): List<Track>
}
