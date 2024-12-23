package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.ui.models.RecipeUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private data class RecipeDetailsViewModelState(val recipe: RecipeUiState) {
    fun toRecipeDetailsUiState(): RecipeDetailsUiState {
        return RecipeDetailsUiState(recipe = recipe)
    }
}

@AssistedFactory
interface RecipeDetailsViewModelFactory {
    fun create(recipeUiState: RecipeUiState): RecipeDetailsViewModel
}

@HiltViewModel(assistedFactory = RecipeDetailsViewModelFactory::class)
class RecipeDetailsViewModel @AssistedInject constructor(
    @Assisted private val recipeUiState: RecipeUiState
) : ViewModel() {

    private val viewModelState = MutableStateFlow(RecipeDetailsViewModelState(recipe = recipeUiState))
    val uiState = viewModelState
        .map(RecipeDetailsViewModelState::toRecipeDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toRecipeDetailsUiState()
        )

}