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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class RecipeDetailsViewModelState(
    val recipe: RecipeUiState,
    val showOpenYouTubeDialog: Boolean = false,
) {
    fun toRecipeDetailsUiState(): RecipeDetailsUiState {
        return RecipeDetailsUiState(
            recipe = recipe,
            showOpenYouTubeDialog = showOpenYouTubeDialog,
        )
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

    private val _oneTimeEvent = MutableSharedFlow<RecipeDetailsOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    fun onAction(action: RecipeDetailsAction) {
        when (action) {
            RecipeDetailsAction.NavigateUp -> navigateUp()
            RecipeDetailsAction.ShowOpenYouTubeDialog -> showOpenYoutubeDialog()
            RecipeDetailsAction.HideOpenYouTubeDialog -> hideOpenYouTubeDialog()
            RecipeDetailsAction.OnLeaveApplicationClicked -> leaveApplication()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _oneTimeEvent.emit(RecipeDetailsOneTimeEvent.NavigateUp)
        }
    }

    private fun showOpenYoutubeDialog() {
        viewModelState.update {
            it.copy(showOpenYouTubeDialog = true)
        }
    }

    private fun hideOpenYouTubeDialog() {
        viewModelState.update {
            it.copy(showOpenYouTubeDialog = false)
        }
    }

    private fun leaveApplication() {
        viewModelState.update {
            it.copy(showOpenYouTubeDialog = false)
        }
        viewModelScope.launch {
            _oneTimeEvent.emit(RecipeDetailsOneTimeEvent.OpenBrowser(recipeUiState.videoUrl))
        }
    }

}