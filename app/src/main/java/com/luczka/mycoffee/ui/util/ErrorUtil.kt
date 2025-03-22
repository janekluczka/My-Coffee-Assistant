package com.luczka.mycoffee.ui.util

import com.luczka.mycoffee.R
import com.luczka.mycoffee.domain.models.ApiError

object ErrorUtil {

    fun getErrorMessageResource(throwable: Throwable): Int {
        return when (throwable) {
            is ApiError.NetworkError -> R.string.error_text_no_network
            is ApiError.ServerError -> R.string.error_text_server_error
            is ApiError.CancelledError -> R.string.error_text_fetch_cancelled
            is ApiError.UnknownError -> R.string.error_text_unknown_error
            else -> R.string.error_text_unknown_error
        }
    }
}