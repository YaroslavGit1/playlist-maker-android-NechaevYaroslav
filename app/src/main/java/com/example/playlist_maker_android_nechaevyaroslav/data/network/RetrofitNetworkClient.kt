package com.example.playlist_maker_android_nechaevyaroslav.data.network

import com.example.playlist_maker_android_nechaevyaroslav.data.dto.TrackDto
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

class RetrofitNetworkClient(
    private val api: ITunesApi,
) : NetworkClient {

    override suspend fun searchTracks(term: String): NetworkResult<List<TrackDto>> = safeCall {
        api.search(term).results.orEmpty()
    }

    private suspend fun <T> safeCall(block: suspend () -> T): NetworkResult<T> = try {
        NetworkResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        NetworkResult.ServerError(e.code())
    } catch (e: IOException) {
        NetworkResult.NetworkError
    }
}
