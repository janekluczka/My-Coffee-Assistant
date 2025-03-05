package com.luczka.mycoffee.ui.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.mappers.toUiState
import com.luczka.mycoffee.domain.repositories.FirebaseRepository
import com.luczka.mycoffee.ui.models.MethodUiState
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class RecipesViewModelState(
    val methodUiState: MethodUiState,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val recipes: List<RecipeUiState>? = null,
) {
    fun toRecipesUiState(): RecipesUiState {
        return if (recipes == null) {
            RecipesUiState.NoRecipes(
                methodUiState = methodUiState,
                showInfoButton = methodUiState.description.isNotBlank(),
                isLoading = isLoading,
                isError = isError,
                errorMessage = errorMessage,
            )
        } else {
            RecipesUiState.HasRecipes(
                methodUiState = methodUiState,
                showInfoButton = methodUiState.description.isNotBlank(),
                isLoading = isLoading,
                isError = isError,
                recipes = recipes,
            )
        }
    }
}

@AssistedFactory
interface RecipesViewModelFactory {
    fun create(methodUiState: MethodUiState): RecipesViewModel
}

@HiltViewModel(assistedFactory = RecipesViewModelFactory::class)
class RecipesViewModel @AssistedInject constructor(
    @Assisted methodUiState: MethodUiState,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        RecipesViewModelState(
            methodUiState = methodUiState,
            isLoading = true
        )
    )
    val uiState = viewModelState
        .map(RecipesViewModelState::toRecipesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toRecipesUiState()
        )

    private val _navigationEvent = MutableSharedFlow<RecipesNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = firebaseRepository.getRecipes(methodId = methodUiState.id)
            when {
                result.isSuccess -> {
                    val recipes = result.getOrNull()?.map { recipeModel ->
                        recipeModel.toUiState()
                    }
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            recipes = recipes,
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

    fun onAction(action: RecipesAction) {
        when (action) {
            RecipesAction.NavigateUp -> navigateUp()
            is RecipesAction.NavigateToRecipeDetails -> navigateToRecipeDetails(action.recipeUiState)
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _navigationEvent.emit(RecipesNavigationEvent.NavigateUp)
        }
    }

    private fun navigateToRecipeDetails(recipeUiState: RecipeUiState) {
        viewModelScope.launch {
            _navigationEvent.emit(RecipesNavigationEvent.NavigateToRecipeDetails(recipeUiState))
        }
    }

}