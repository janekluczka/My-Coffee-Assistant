package com.luczka.mycoffee.ui.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.mappers.toUiState
import com.luczka.mycoffee.domain.repository.FirebaseRepository
import com.luczka.mycoffee.ui.models.RecipeUiState
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

sealed interface RecipesUiState {
    val methodId: String
    val title: String
    val isLoading: Boolean

    data class NoRecipes(
        override val methodId: String,
        override val title: String,
        override val isLoading: Boolean,
        val errorMessage: String,
    ) : RecipesUiState

    data class HasRecipes(
        override val methodId: String,
        override val title: String,
        override val isLoading: Boolean,
        val recipes: List<RecipeUiState>,
    ) : RecipesUiState
}

private data class RecipesViewModelState(
    val methodId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val recipes: List<RecipeUiState>? = null,
) {
    fun toRecipesUiState(): RecipesUiState {
        return if (recipes == null) {
            RecipesUiState.NoRecipes(
                methodId = methodId,
                title = methodId,
                isLoading = isLoading,
                errorMessage = errorMessage,
            )
        } else {
            RecipesUiState.HasRecipes(
                methodId = methodId,
                title = methodId,
                isLoading = isLoading,
                recipes = recipes,
            )
        }
    }
}

@AssistedFactory
interface RecipesViewModelFactory {
    fun create(methodId: String): RecipesViewModel
}

@HiltViewModel(assistedFactory = RecipesViewModelFactory::class)
class RecipesViewModel @AssistedInject constructor(
    @Assisted methodId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        RecipesViewModelState(
            methodId = methodId,
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

    init {
        viewModelScope.launch {
            firebaseRepository.getRecipes(
                methodId = methodId,
                onSuccess = { recipeModels ->
                    val recipes = recipeModels.map { it.toUiState() }
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            recipes = recipes,
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

}