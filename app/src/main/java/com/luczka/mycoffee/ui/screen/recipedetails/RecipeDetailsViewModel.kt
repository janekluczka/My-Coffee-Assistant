package com.luczka.mycoffee.ui.screen.recipedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repository.FirebaseRepository
import com.luczka.mycoffee.ui.model.RecipeUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

@AssistedFactory
interface RecipeDetailsViewModelFactory {
    fun create(recipeId: String): RecipeDetailsViewModel
}

@HiltViewModel(assistedFactory = RecipeDetailsViewModelFactory::class)
class RecipeDetailsViewModel @AssistedInject constructor(
    @Assisted private val recipeId: String,
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