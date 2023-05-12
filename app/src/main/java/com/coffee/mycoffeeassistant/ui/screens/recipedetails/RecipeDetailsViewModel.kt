package com.coffee.mycoffeeassistant.ui.screens.recipedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coffee.mycoffeeassistant.ui.model.RecipeUiState

class RecipeDetailsViewModel() : ViewModel() {

    var recipeUiState by mutableStateOf(RecipeUiState(youtubeId = "tltBHjmIUJ0"))
        private set

}