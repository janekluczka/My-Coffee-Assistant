package com.luczka.mycoffee.ui.screens.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.mappers.toUiState
import com.luczka.mycoffee.domain.usecases.GetRecipesUseCase
import com.luczka.mycoffee.ui.models.CategoryUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
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
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val recipes: List<RecipeUiState>? = null,
    val openMethodInfoDialog: Boolean = false,
) {
    fun toRecipeListUiState(): RecipeListUiState {
        return if (recipes == null) {
            RecipeListUiState.NoRecipes(
                categoryUiState = categoryUiState,
                hasInfoButton = categoryUiState.description.isNotBlank(),
                isLoading = isLoading,
                isError = isError,
                errorMessage = errorMessage,
                openMethodInfoDialog = openMethodInfoDialog
            )
        } else {
            RecipeListUiState.HasRecipes(
                categoryUiState = categoryUiState,
                hasInfoButton = categoryUiState.description.isNotBlank(),
                isLoading = isLoading,
                isError = isError,
                recipes = recipes,
                openMethodInfoDialog = openMethodInfoDialog
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
        RecipesListViewModelState(categoryUiState = categoryUiState,)
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
                it.copy(isLoading = true)
            }

            val result = getRecipesUseCase(methodId = _viewModelState.value.categoryUiState.id)

            when {
                result.isSuccess -> {
                    val recipes = result.getOrNull()?.toUiState()
                    _viewModelState.update {
                        it.copy(
                            isLoading = false,
                            recipes = recipes,
                        )
                    }
                }

                result.isFailure -> {
                    _viewModelState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: RecipeListAction) {
        when (action) {
            RecipeListAction.NavigateUp -> navigateUp()
            is RecipeListAction.NavigateToRecipeDetails -> navigateToRecipeDetails(action.recipeUiState)
            RecipeListAction.ShowMethodInfoDialog -> showMethodInfoDialog()
            RecipeListAction.HideMethodInfoDialog -> hideMethodInfoDialog()
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