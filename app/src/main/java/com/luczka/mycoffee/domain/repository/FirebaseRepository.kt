package com.luczka.mycoffee.domain.repository

import com.luczka.mycoffee.domain.models.Method
import com.luczka.mycoffee.domain.models.Recipe

interface FirebaseRepository {

    fun getMethods(onSuccess: (List<Method>) -> Unit, onError: (String) -> Unit)

    fun getRecipes(methodId: String, onSuccess: (List<Recipe>) -> Unit, onError: (String) -> Unit)

    fun getRecipe(youtubeId: String, onSuccess: (Recipe) -> Unit)

}