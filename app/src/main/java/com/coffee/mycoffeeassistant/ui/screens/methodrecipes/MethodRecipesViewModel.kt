package com.coffee.mycoffeeassistant.ui.screens.methodrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.screens.MethodRecipesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MethodRecipesViewModel(
    private val methodId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MethodRecipesUiState())
    val uiState: StateFlow<MethodRecipesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseRepository.getRecipes(methodId = methodId) { recipeList ->
                val title = methodId.replaceFirstChar { it.uppercase() }
                val recipesUiStateList = recipeList.map { it.toRecipeCardUiState() }
                _uiState.update {
                    it.copy(
                        title = title,
                        recipesList = recipesUiStateList
                    )
                }
            }
        }
    }

}