package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TrackDto

interface NetworkClient {

    suspend fun searchTracks(term: String): NetworkResult<List<TrackDto>>
}
