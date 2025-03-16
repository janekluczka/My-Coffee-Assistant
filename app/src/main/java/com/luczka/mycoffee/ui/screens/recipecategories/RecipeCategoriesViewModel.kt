package com.luczka.mycoffee.ui.screens.recipecategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.GetMethodsUseCase
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.MethodUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    fun toRecipeCategoriesUiState(): RecipeCategoriesUiState {
        return when {
            isError -> RecipeCategoriesUiState.IsError(
                isError = true,
                isLoading = isLoading,
                methods = methods ?: emptyList()
            )

            methods == null -> RecipeCategoriesUiState.NoRecipeCategories(
                isError = false,
                isLoading = isLoading,
                errorMessage = errorMessage,
                methods = emptyList()
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
    private val getMethodsUseCase: GetMethodsUseCase
) : ViewModel() {

    private val viewModelState = MutableStateFlow(MethodsViewModelState(isLoading = true))
    val uiState = viewModelState
        .onStart { loadCategories() }
        .map(MethodsViewModelState::toRecipeCategoriesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = viewModelState.value.toRecipeCategoriesUiState()
        )

    private val _navigationEvent = MutableSharedFlow<RecipeCategoriesNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private fun loadCategories() {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(isLoading = true)
            }

            val result = getMethodsUseCase()

            when {
                result.isSuccess -> {
                    val methodUiStateListSorted = result.getOrNull()?.toUiState()
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

    fun onAction(action: RecipeCategoriesAction) {
        when (action) {
            is RecipeCategoriesAction.NavigateToRecipes -> navigateToRecipes(action.methodUiState)
        }
    }

    private fun navigateToRecipes(methodUiState: MethodUiState) {
        viewModelScope.launch {
            _navigationEvent.emit(RecipeCategoriesNavigationEvent.NavigateToMethodDetails(methodUiState))
        }
    }

}