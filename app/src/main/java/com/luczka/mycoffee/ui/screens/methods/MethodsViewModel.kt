package com.luczka.mycoffee.ui.screens.methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repositories.FirebaseRepository
import com.luczka.mycoffee.ui.components.MethodUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MethodsUiState {
    val isLoading: Boolean

    data class NoMethods(
        override val isLoading: Boolean,
        val errorMessage: String,
    ) : MethodsUiState

    data class HasMethods(
        override val isLoading: Boolean,
        val methods: List<MethodUiState>
    ) : MethodsUiState
}

private data class MethodsViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val methods: List<MethodUiState>? = null
) {
    fun toMethodUiState(): MethodsUiState {
        return if (methods == null) {
            MethodsUiState.NoMethods(
                isLoading = isLoading,
                errorMessage = errorMessage,
            )
        } else {
            MethodsUiState.HasMethods(
                isLoading = isLoading,
                methods = methods
            )
        }
    }
}

class MethodsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val viewModelState = MutableStateFlow(MethodsViewModelState(isLoading = true))
    val uiState = viewModelState
        .map(MethodsViewModelState::toMethodUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toMethodUiState()
        )

    init {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            firebaseRepository.getMethods(
                onSuccess = { methodList ->
                    val methodUiStateListSorted = methodList
                        .map { it.toMethodUiState() }
                        .sortedBy { it.name }
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            methods = methodUiStateListSorted
                        )
                    }
                },
                onError = { errorMessage ->
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
            )
        }
    }

}