package com.luczka.mycoffee.domain.models

sealed class ApiError : Exception() {

    data object NetworkError : ApiError() {
        private fun readResolve(): Any = NetworkError
    }

    data object ServerError : ApiError() {
        private fun readResolve(): Any = ServerError
    }

    data object CancelledError : ApiError() {
        private fun readResolve(): Any = CancelledError
    }

    data object UnknownError : ApiError() {
        private fun readResolve(): Any = UnknownError
    }
}