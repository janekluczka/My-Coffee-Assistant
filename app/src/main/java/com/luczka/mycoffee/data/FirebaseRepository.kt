package com.luczka.mycoffee.data

import com.luczka.mycoffee.models.Method
import com.luczka.mycoffee.models.Recipe

interface FirebaseRepository {

    fun getMethods(onSuccess: (List<Method>) -> Unit, onError: (String) -> Unit)

    fun getRecipes(methodId: String, onSuccess: (List<Recipe>) -> Unit, onError: (String) -> Unit)

    fun getRecipe(youtubeId: String, onSuccess: (Recipe) -> Unit)

}