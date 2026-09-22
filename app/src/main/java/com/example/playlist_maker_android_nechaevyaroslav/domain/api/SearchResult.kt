package com.example.playlist_maker_android_nechaevyaroslav.domain.api

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track

sealed class SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult()

    object ServerError : SearchResult()

    object NetworkError : SearchResult()
}
