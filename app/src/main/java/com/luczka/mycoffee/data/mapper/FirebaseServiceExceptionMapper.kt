package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.remote.model.FirebaseServiceException
import com.luczka.mycoffee.domain.models.ApiError

fun FirebaseServiceException.toModel(): ApiError {
    return when (this) {
        is FirebaseServiceException.CacheDataException -> ApiError.NetworkError
        is FirebaseServiceException.ServerException -> ApiError.ServerError
        is FirebaseServiceException.CancelledException -> ApiError.CancelledError
        is FirebaseServiceException.UnknownException -> ApiError.UnknownError
    }
}