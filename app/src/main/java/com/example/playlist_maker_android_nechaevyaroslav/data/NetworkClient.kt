package com.example.playlist_maker_android_nechaevyaroslav.data

import com.example.playlist_maker_android_nechaevyaroslav.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}
