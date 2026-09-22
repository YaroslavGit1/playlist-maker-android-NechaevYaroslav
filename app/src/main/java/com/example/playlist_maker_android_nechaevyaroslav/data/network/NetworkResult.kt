package com.example.playlist_maker_android_nechaevyaroslav.data.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()

    data class ServerError(val code: Int) : NetworkResult<Nothing>()

    object NetworkError : NetworkResult<Nothing>()
}
