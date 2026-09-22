package com.example.playlist_maker_android_nechaevyaroslav.ui.view_model

import com.example.playlist_maker_android_nechaevyaroslav.domain.model.Track

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    object ServerError : SearchState()
    object NetworkError : SearchState()
}
