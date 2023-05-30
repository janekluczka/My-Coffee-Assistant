package com.coffee.mycoffeeassistant.ui.screens.methodrecipes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.RecipeUiState
import kotlinx.coroutines.launch

class MethodRecipesViewModel(private val firebaseRepository: FirebaseRepository): ViewModel() {

    var recipeUiStateList by mutableStateOf(emptyList<RecipeUiState>())
        private set

    fun getRecipeUiStateList(methodId: String) {
        viewModelScope.launch {
            firebaseRepository.getRecipes(methodId = methodId) { recipeList ->
                recipeUiStateList = recipeList
            }
        }
    }

}