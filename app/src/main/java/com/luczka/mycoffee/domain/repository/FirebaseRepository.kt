package com.luczka.mycoffee.domain.repository

import com.luczka.mycoffee.domain.model.Method
import com.luczka.mycoffee.domain.model.Recipe

interface FirebaseRepository {

    fun getMethods(onSuccess: (List<Method>) -> Unit, onError: (String) -> Unit)

    fun getRecipes(methodId: String, onSuccess: (List<Recipe>) -> Unit, onError: (String) -> Unit)

    fun getRecipe(youtubeId: String, onSuccess: (Recipe) -> Unit)

}