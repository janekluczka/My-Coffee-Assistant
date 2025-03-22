package com.luczka.mycoffee.data.remote.model

sealed class FirebaseServiceException(message: String?) : Exception(message) {
    class CacheDataException(message: String? = "Data is from cache, not fetching from server.") : FirebaseServiceException(message)
    class ServerException(message: String?) : FirebaseServiceException(message)
    class CancelledException(message: String?) : FirebaseServiceException(message)
    class UnknownException(message: String?) : FirebaseServiceException(message)
}