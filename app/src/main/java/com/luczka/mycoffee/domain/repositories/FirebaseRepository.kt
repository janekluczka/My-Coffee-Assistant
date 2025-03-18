package com.luczka.mycoffee.domain.repositories

import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.domain.models.RecipeModel

interface FirebaseRepository {

    suspend fun getCategories(): Result<List<MethodModel>>

    suspend fun getRecipes(methodId: String): Result<List<RecipeModel>>
}