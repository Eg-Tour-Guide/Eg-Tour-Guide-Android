package com.egtourguide.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeCall(call: suspend () -> T): Flow<ResultWrapper<T>> = flow {
    emit(ResultWrapper.Loading)
    try {
        emit(ResultWrapper.Success(data = call.invoke()))
    } catch (e: HttpException) {
        // TODO: Extract message!!
        e.printStackTrace()
        emit(ResultWrapper.Failure(message = e.message ?: ""))
    } catch (e: IOException) {
        e.printStackTrace()
        emit(ResultWrapper.NetworkError)
    } catch (e: Exception) {
        e.printStackTrace()
        emit(ResultWrapper.Failure(message = "An error happened! Please try again later."))
    }
}