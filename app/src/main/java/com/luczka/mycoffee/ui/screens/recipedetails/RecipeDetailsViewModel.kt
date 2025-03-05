package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

sealed class RecipeDetailsNavigationEvent {
    data object NavigateUp : RecipeDetailsNavigationEvent()
}

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

    private val _navigationEvent = MutableSharedFlow<RecipeDetailsNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onAction(action: RecipeDetailsAction) {
        when (action) {
            RecipeDetailsAction.NavigateUp -> navigateUp()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _navigationEvent.emit(RecipeDetailsNavigationEvent.NavigateUp)
        }
    }

}