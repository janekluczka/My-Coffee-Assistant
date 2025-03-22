package com.luczka.mycoffee.ui.screens.recipecategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.R
import com.luczka.mycoffee.domain.usecases.GetCategoriesUseCase
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CategoryUiState
import com.luczka.mycoffee.ui.util.ErrorUtil
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

private data class RecipeCategoriesViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessageRes: Int = R.string.error_text_unknown_error,
    val categories: List<CategoryUiState>? = null
) {
    fun toRecipeCategoriesUiState(): RecipeCategoriesUiState {
        return if (categories == null) {
            RecipeCategoriesUiState.NoRecipeCategories(
                isError = isError,
                isLoading = isLoading,
                errorMessageRes = errorMessageRes,
            )
        } else {
            RecipeCategoriesUiState.HasRecipeCategories(
                isLoading = isLoading,
                isError = isError,
                errorMessageRes = errorMessageRes,
                categories = categories
            )
        }
    }
}

@HiltViewModel
class RecipeCategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(RecipeCategoriesViewModelState(isLoading = true))
    val uiState = _viewModelState
        .onStart { loadCategories() }
        .map(RecipeCategoriesViewModelState::toRecipeCategoriesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _viewModelState.value.toRecipeCategoriesUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<RecipeCategoriesOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    private fun loadCategories() {
        viewModelScope.launch {
            _viewModelState.update {
                it.copy(
                    isLoading = true,
                    isError = false
                )
            }

            getCategoriesUseCase().fold(
                onSuccess = { categoryModelList ->
                    categoryModelList
                        .toUiState()
                        .sorted()
                        .also { categoryUiStateList ->
                            _viewModelState.update {
                                it.copy(
                                    isLoading = false,
                                    categories = categoryUiStateList
                                )
                            }
                        }
                },
                onFailure = { exception ->
                    _viewModelState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessageRes = ErrorUtil.getErrorMessageResource(exception)
                        )
                    }
                }
            )
        }
    }

    fun onAction(action: RecipeCategoriesAction) {
        when (action) {
            is RecipeCategoriesAction.NavigateToRecipes -> navigateToRecipes(action.categoryUiState)
            RecipeCategoriesAction.OnRetryClicked -> loadCategories()
        }
    }

    private fun navigateToRecipes(categoryUiState: CategoryUiState) {
        viewModelScope.launch {
            _oneTimeEvent.emit(RecipeCategoriesOneTimeEvent.NavigateToMethodDetails(categoryUiState))
        }
    }
}