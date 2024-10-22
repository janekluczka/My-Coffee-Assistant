package com.luczka.mycoffee.domain.repository

import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.domain.models.RecipeModel

interface FirebaseRepository {

    fun getMethods(
        onSuccess: (List<MethodModel>) -> Unit,
        onError: (String) -> Unit
    )

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