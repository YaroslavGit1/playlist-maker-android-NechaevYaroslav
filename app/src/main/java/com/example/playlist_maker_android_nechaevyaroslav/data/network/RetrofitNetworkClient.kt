package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.NetworkClient
import com.example.playlist_maker_android_nechaevyaroslav.data.dto.BaseResponse

class RetrofitNetworkClient : NetworkClient {
    override fun doRequest(dto: Any): BaseResponse = BaseResponse(400)
}
