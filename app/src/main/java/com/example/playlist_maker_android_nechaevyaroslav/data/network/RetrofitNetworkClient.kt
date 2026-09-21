package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.Storage
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.BaseResponse
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TracksSearchResponse

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse = when (dto) {
        is TracksSearchRequest -> TracksSearchResponse(
            tracks = storage.tracks.filter { track ->
                track.trackName.contains(dto.expression, ignoreCase = true) ||
                    track.artistName.contains(dto.expression, ignoreCase = true)
            },
        )

        else -> BaseResponse(BAD_REQUEST)
    }

    private companion object {
        const val BAD_REQUEST = 400
    }
}