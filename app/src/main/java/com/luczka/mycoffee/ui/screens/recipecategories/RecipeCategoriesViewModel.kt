package com.luczka.mycoffee.ui.screens.recipecategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repositories.FirebaseRepository
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.MethodUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class MethodsViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val methods: List<MethodUiState>? = null
) {
    fun toMethodUiState(): RecipeCategoriesUiState {
        return when {
            isError -> RecipeCategoriesUiState.IsError(
                isError = true,
                isLoading = isLoading
            )

            methods == null -> RecipeCategoriesUiState.NoRecipeCategories(
                isLoading = isLoading,
                isError = false,
                errorMessage = errorMessage
            )

            else -> RecipeCategoriesUiState.HasRecipeCategories(
                isLoading = isLoading,
                isError = false,
                methods = methods
            )
        }
    }
}

@HiltViewModel
class RecipeCategoriesViewModel @Inject constructor(
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
            val result = firebaseRepository.getMethods()
            when {
                result.isSuccess -> {
                    val methodUiStateListSorted = result
                        .getOrDefault(emptyList())
                        .map { it.toUiState() }
                        .sortedBy { it.name }
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            methods = methodUiStateListSorted
                        )
                    }
                }

                result.isFailure -> {
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            }
        }
    }

}