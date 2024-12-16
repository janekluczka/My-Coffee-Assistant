package com.luczka.mycoffee.domain.repositories

import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.domain.models.RecipeModel

interface FirebaseRepository {

    suspend fun getMethods() : Result<List<MethodModel>>

    fun getRecipes(
        methodId: String,
        onSuccess: (List<RecipeModel>) -> Unit,
        onError: (String) -> Unit
    )

    fun getRecipe(
        youtubeId: String,
        onSuccess: (RecipeModel) -> Unit
    )

}