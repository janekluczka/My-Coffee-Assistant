package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repositories.FirebaseRepository
import com.luczka.mycoffee.ui.model.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RecipeDetailsUiState {
    val isLoading: Boolean

    data class IsLoading(
        override val isLoading: Boolean
    ) : RecipeDetailsUiState

    data class HasData(
        override val isLoading: Boolean,
        val recipe: RecipeUiState
    ) : RecipeDetailsUiState
}

private data class RecipeDetailsViewModelState(
    val isLoading: Boolean = false,
    val recipe: RecipeUiState? = null
) {
    fun toRecipeDetailsUiState(): RecipeDetailsUiState {
        return if (recipe == null) {
            RecipeDetailsUiState.IsLoading(
                isLoading = isLoading
            )
        } else {
            RecipeDetailsUiState.HasData(
                isLoading = isLoading,
                recipe = recipe
            )
        }
    }
}

class RecipeDetailsViewModel(
    private val recipeId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(RecipeDetailsViewModelState(isLoading = true))
    val uiState = viewModelState
        .map(RecipeDetailsViewModelState::toRecipeDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toRecipeDetailsUiState()
        )

    init {
        viewModelScope.launch {
            firebaseRepository.getRecipe(
                youtubeId = recipeId,
                onSuccess = { recipe ->
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            recipe = recipe.toRecipeDetailsUiState()
                        )
                    }
                }
            )
        }
    }

}