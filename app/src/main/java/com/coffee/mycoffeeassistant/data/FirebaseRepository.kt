package com.coffee.mycoffeeassistant.data

import com.coffee.mycoffeeassistant.ui.model.MethodUiState
import com.coffee.mycoffeeassistant.ui.model.RecipeDetailsUiState
import com.coffee.mycoffeeassistant.ui.model.RecipeUiState

interface FirebaseRepository {

    fun getMethods(onSuccess: (MutableList<MethodUiState>) -> Unit)

    fun getRecipes(methodId: String, onSuccess: (MutableList<RecipeUiState>) -> Unit)

    fun getRecipe(youtubeId: String, onSuccess: (RecipeDetailsUiState) -> Unit)

}