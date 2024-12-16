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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RecipesUiState {
    val methodUiState: MethodUiState
    val showInfoButton: Boolean
    val isLoading: Boolean

    data class NoRecipes(
        override val methodUiState: MethodUiState,
        override val showInfoButton: Boolean,
        override val isLoading: Boolean,
        val errorMessage: String,
    ) : RecipesUiState

    data class HasRecipes(
        override val methodUiState: MethodUiState,
        override val showInfoButton: Boolean,
        override val isLoading: Boolean,
        val recipes: List<RecipeUiState>,
    ) : RecipesUiState
}

private data class RecipesViewModelState(
    val methodUiState: MethodUiState,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val recipes: List<RecipeUiState>? = null,
) {
    fun toRecipesUiState(): RecipesUiState {
        return if (recipes == null) {
            RecipesUiState.NoRecipes(
                methodUiState = methodUiState,
                showInfoButton = methodUiState.description.isNotBlank(),
                isLoading = isLoading,
                errorMessage = errorMessage,
            )
        } else {
            RecipesUiState.HasRecipes(
                methodUiState = methodUiState,
                showInfoButton = methodUiState.description.isNotBlank(),
                isLoading = isLoading,
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

    init {
        viewModelScope.launch {
            firebaseRepository.getRecipes(
                methodId = methodUiState.id,
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