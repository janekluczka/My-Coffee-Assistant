package com.luczka.mycoffee.ui.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repositories.FirebaseRepository
import com.luczka.mycoffee.ui.model.RecipeDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RecipesUiState {
    val title: String
    val isLoading: Boolean
    val selectedRecipe: RecipeDetailsUiState?
    val showDetails: Boolean

    data class NoRecipes(
        override val title: String,
        override val isLoading: Boolean,
        val errorMessage: String,
        override val selectedRecipe: RecipeDetailsUiState? = null,
        override val showDetails: Boolean = false,
    ) : RecipesUiState

    data class HasRecipes(
        override val title: String,
        override val isLoading: Boolean,
        val recipes: List<RecipeDetailsUiState>,
        override val selectedRecipe: RecipeDetailsUiState?,
        override val showDetails: Boolean
    ) : RecipesUiState
}

private data class RecipesViewModelState(
    val title: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val recipes: List<RecipeDetailsUiState>? = null,
    val selectedRecipeYoutubeId: String? = null,
    val showDetails: Boolean = false
) {
    fun toRecipesUiState(): RecipesUiState {
        return if (recipes == null) {
            RecipesUiState.NoRecipes(
                title = title,
                isLoading = isLoading,
                errorMessage = errorMessage,
            )
        } else {
            RecipesUiState.HasRecipes(
                title = title,
                isLoading = isLoading,
                recipes = recipes,
                selectedRecipe = recipes.find { it.youtubeId == selectedRecipeYoutubeId },
                showDetails = showDetails
            )
        }
    }
}

class RecipesViewModel(
    private val methodId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(RecipesViewModelState(isLoading = true))
    val uiState = viewModelState
        .map(RecipesViewModelState::toRecipesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toRecipesUiState()
        )

    init {
        viewModelState.update {
            it.copy(
                title = methodId.replaceFirstChar { char -> char.uppercase() },
                isLoading = true
            )
        }
        viewModelScope.launch {
            firebaseRepository.getRecipes(
                methodId = methodId,
                onSuccess = { recipeList ->
                    val recipes = recipeList.map { it.toRecipeDetailsUiState() }
                    val firstRecipeYoutubeId = recipes.firstOrNull()?.youtubeId
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            recipes = recipes,
                            selectedRecipeYoutubeId = firstRecipeYoutubeId,
                            showDetails = false
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

    fun selectRecipe(youtubeId: String?, showDetails: Boolean) {
        viewModelState.update {
            it.copy(
                selectedRecipeYoutubeId = youtubeId,
                showDetails = showDetails
            )
        }
    }

    fun onHideDetails() {
        viewModelState.update { it.copy(showDetails = false) }
    }

}