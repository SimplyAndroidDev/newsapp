package com.android.newsapp.core.common

sealed class NetworkResult<T> {
    data class Success<T>(
        val result: T
    ) : NetworkResult<T>()

    data class Error<T>(
        val throwable: Throwable? = null,
        val message: String?
    ) : NetworkResult<T>()
}