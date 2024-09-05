package com.egtourguide.core.utils

sealed class ResultWrapper<out T> {
    data object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Failure(val message: String = "") : ResultWrapper<Nothing>()
    data object NetworkError : ResultWrapper<Nothing>()
}