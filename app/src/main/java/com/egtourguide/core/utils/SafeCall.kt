package com.egtourguide.core.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeCall(call: suspend () -> T): Flow<ResultWrapper<T>> = flow {
    emit(ResultWrapper.Loading)
    try {
        emit(ResultWrapper.Success(data = call.invoke()))
    } catch (e: HttpException) {
        e.printStackTrace()
        emit(ResultWrapper.Failure(message = e.message ?: ""))
    } catch (e: IOException) {
        e.printStackTrace()
        emit(ResultWrapper.Failure(message = "Please check your internet connection!"))
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("```TAG```", "safeCall: $e")
        emit(ResultWrapper.Failure(message = "An error happened! Please try again later."))
    }
}