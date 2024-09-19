package com.luczka.mycoffee.ui.screen.methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repository.FirebaseRepository
import com.luczka.mycoffee.ui.model.MethodUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

private data class MethodsViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val methods: List<MethodUiState>? = null
) {
    fun toMethodUiState(): MethodsUiState {
        return when {
            isError -> MethodsUiState.IsError(
                isError = true,
                isLoading = isLoading
            )

            methods == null -> MethodsUiState.NoMethods(
                isLoading = isLoading,
                isError = false,
                errorMessage = errorMessage
            )

            else -> MethodsUiState.HasMethods(
                isLoading = isLoading,
                isError = false,
                methods = methods
            )
        }
    }
}

@HiltViewModel
class MethodsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

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

//                    viewModelState.update {
//                        it.copy(
//                            isLoading = false,
//                            isError = true
//                        )
//                    }
                },
                onError = { errorMessage ->
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            )
        }
    }

}