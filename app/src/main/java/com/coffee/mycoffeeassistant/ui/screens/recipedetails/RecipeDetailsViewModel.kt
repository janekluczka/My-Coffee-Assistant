package com.coffee.mycoffeeassistant.ui.screens.recipedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.RecipeDetailsUiState
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    var recipeDetailsUiState by mutableStateOf(RecipeDetailsUiState())
        private set

    fun getRecipe(youtubeId: String) {
        viewModelScope.launch {
            firebaseRepository.getRecipe(youtubeId = youtubeId) { recipe ->
                recipeDetailsUiState = recipe
            }
        }
    }

}