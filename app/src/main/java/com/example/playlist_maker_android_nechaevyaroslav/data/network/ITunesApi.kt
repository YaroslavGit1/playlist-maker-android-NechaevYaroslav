package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") term: String): TracksSearchResponse
}
