package com.luczka.mycoffee.ui.screens.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.domain.usecases.GetRecipesUseCase
import com.luczka.mycoffee.ui.models.CategoryUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
import com.luczka.mycoffee.ui.util.ErrorUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

private data class RecipesListViewModelState(
    val categoryUiState: CategoryUiState,
    val openMethodInfoDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessageRes: Int = R.string.error_text_unknown_error,
    val recipes: List<RecipeUiState>? = null,
) {
    fun toRecipeListUiState(): RecipeListUiState {
        return if (recipes == null) {
            RecipeListUiState.NoRecipes(
                categoryUiState = categoryUiState,
                hasInfoButton = categoryUiState.description.isNotBlank(),
                openMethodInfoDialog = openMethodInfoDialog,
                isLoading = isLoading,
                isError = isError,
                errorMessageRes = errorMessageRes
            )
        } else {
            RecipeListUiState.HasRecipes(
                categoryUiState = categoryUiState,
                hasInfoButton = categoryUiState.description.isNotBlank(),
                openMethodInfoDialog = openMethodInfoDialog,
                isError = isError,
                isLoading = isLoading,
                errorMessageRes = errorMessageRes,
                recipes = recipes,
            )
        }
    }
}

@AssistedFactory
interface RecipeListViewModelFactory {
    fun create(categoryUiState: CategoryUiState): RecipesListViewModel
}

@HiltViewModel(assistedFactory = RecipeListViewModelFactory::class)
class RecipesListViewModel @AssistedInject constructor(
    @Assisted categoryUiState: CategoryUiState,
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(
        RecipesListViewModelState(categoryUiState = categoryUiState)
    )
    val uiState = _viewModelState
        .onStart { loadRecipes() }
        .map(RecipesListViewModelState::toRecipeListUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _viewModelState.value.toRecipeListUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<RecipeListOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    private fun loadRecipes() {
        viewModelScope.launch {
            _viewModelState.update {
                it.copy(
                    isLoading = true,
                    isError = false
                )
            }

            getRecipesUseCase(methodId = _viewModelState.value.categoryUiState.id).fold(
                onSuccess = { recipeModelList ->
                    recipeModelList
                        .toUiState()
                        .also { recipeUiStateList ->
                            _viewModelState.update {
                                it.copy(
                                    isLoading = false,
                                    recipes = recipeUiStateList,
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

    fun onAction(action: RecipeListAction) {
        when (action) {
            RecipeListAction.NavigateUp -> navigateUp()
            is RecipeListAction.NavigateToRecipeDetails -> navigateToRecipeDetails(action.recipeUiState)
            RecipeListAction.ShowMethodInfoDialog -> showMethodInfoDialog()
            RecipeListAction.HideMethodInfoDialog -> hideMethodInfoDialog()
            RecipeListAction.OnRetryClicked -> loadRecipes()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _oneTimeEvent.emit(RecipeListOneTimeEvent.NavigateUp)
        }
    }

    private fun navigateToRecipeDetails(recipeUiState: RecipeUiState) {
        viewModelScope.launch {
            _oneTimeEvent.emit(RecipeListOneTimeEvent.NavigateToRecipeDetails(recipeUiState))
        }
    }

    private fun showMethodInfoDialog() {
        _viewModelState.update {
            it.copy(openMethodInfoDialog = true)
        }
    }

    private fun hideMethodInfoDialog() {
        _viewModelState.update {
            it.copy(openMethodInfoDialog = false)
        }
    }
}