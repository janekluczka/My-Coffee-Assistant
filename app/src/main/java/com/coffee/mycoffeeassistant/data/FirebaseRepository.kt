package com.coffee.mycoffeeassistant.data

import com.coffee.mycoffeeassistant.models.Method
import com.coffee.mycoffeeassistant.models.Recipe

interface FirebaseRepository {

    fun getMethods(onSuccess: (List<Method>) -> Unit)

    fun getRecipes(methodId: String, onSuccess: (List<Recipe>) -> Unit)

    fun getRecipe(youtubeId: String, onSuccess: (Recipe) -> Unit)

}