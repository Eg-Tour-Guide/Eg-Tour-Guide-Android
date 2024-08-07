package com.egtourguide.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<ResultWrapper<T>>.onResponse(
    onLoading: () -> Unit,
    onSuccess: (T) -> Unit,
    onFailure: (String) -> Unit,
    onNetworkError: () -> Unit
) {
    this.collectLatest { response ->
        when (response) {
            is ResultWrapper.Loading -> onLoading()
            is ResultWrapper.Success -> onSuccess(response.data)
            is ResultWrapper.Failure -> onFailure(response.message)
            is ResultWrapper.NetworkError -> onNetworkError()
        }
    }
}