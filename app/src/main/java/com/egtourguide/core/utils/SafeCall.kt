package com.egtourguide.core.utils

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeCall(call: suspend () -> T): Flow<ResultWrapper<T>> = flow {
    emit(ResultWrapper.Loading)
    try {
        emit(ResultWrapper.Success(data = call.invoke()))
    } catch (e: IOException) {
        e.printStackTrace()
        emit(ResultWrapper.NetworkError)
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        emit(getNetworkErrorFromThrowable(throwable))
    }
}

private fun getNetworkErrorFromThrowable(throwable: Throwable): ResultWrapper.Failure {
    return when (throwable) {
        is HttpException -> {
            val errorResponse = convertErrorBody(throwable)
            ResultWrapper.Failure(errorResponse)
        }

        else -> {
            ResultWrapper.Failure(message = throwable.message.toString())
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String {
    return try {
        val errorBody = throwable.response()?.errorBody()?.string() ?: ""
        val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
        errorModel.data.message
    } catch (exception: Exception) {
        ""
    }
}

data class ErrorModel(
    val status: String,
    val data: Data
) {
    data class Data(
        val message: String
    )
}