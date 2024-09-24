package com.luczka.mycoffee.ui.screens.methods

import com.luczka.mycoffee.ui.models.MethodUiState

sealed interface MethodsUiState {
    val isLoading: Boolean
    val isError: Boolean

    data class IsError(
        override val isLoading: Boolean,
        override val isError: Boolean,
    ) : MethodsUiState

    data class NoMethods(
        override val isLoading: Boolean,
        override val isError: Boolean,
        val errorMessage: String,
    ) : MethodsUiState

    data class HasMethods(
        override val isLoading: Boolean,
        override val isError: Boolean,
        val methods: List<MethodUiState>
    ) : MethodsUiState
}