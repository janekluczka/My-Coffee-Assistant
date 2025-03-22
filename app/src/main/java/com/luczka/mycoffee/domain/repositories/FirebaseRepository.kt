package com.luczka.mycoffee.domain.repositories

import com.luczka.mycoffee.domain.models.CategoryModel
import com.luczka.mycoffee.domain.models.RecipeModel

interface FirebaseRepository {

    suspend fun getCategories(): Result<List<CategoryModel>>

    suspend fun getRecipes(methodId: String): Result<List<RecipeModel>>
}