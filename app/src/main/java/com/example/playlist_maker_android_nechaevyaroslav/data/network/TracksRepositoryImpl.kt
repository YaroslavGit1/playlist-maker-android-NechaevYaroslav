package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TracksSearchResponse
import com.example.playlist_maker_android_nechaevyaroslav.domain.api.TracksRepository
import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracksContains(expression: String): List<Track> {
        delay(NETWORK_DELAY_MILLIS)

        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response !is TracksSearchResponse) return emptyList()

        return response.tracks.map { trackDto ->
            val seconds = trackDto.trackTimeMillis / 1000
            val minutes = seconds / 60
            val trackTime = "%02d:%02d".format(minutes, seconds % 60)
            Track(trackDto.id, trackDto.trackName, trackDto.artistName, trackTime)
        }
    }

    private companion object {
        const val NETWORK_DELAY_MILLIS = 3000L
    }
}