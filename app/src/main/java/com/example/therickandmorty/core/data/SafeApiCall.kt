package com.example.therickandmorty.core.data

import com.example.therickandmorty.core.domain.DataError
import com.example.therickandmorty.core.domain.Result
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

suspend inline fun <T> safeApiCall(execute: suspend () -> T): Result<T, DataError.Remote> =
    try {
        Result.Success(execute())
    } catch (e: SocketTimeoutException) {
        Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: IOException) {
        // Sem internet, DNS error, etc
        Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: HttpException) {
        when (e.code()) {
            408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)

            429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)

            in 500..599 -> Result.Error(DataError.Remote.SERVER)

            else -> Result.Error(DataError.Remote.UNKNOWN)
        }
    } catch (e: Exception) {
        Result.Error(DataError.Remote.UNKNOWN)
    }
