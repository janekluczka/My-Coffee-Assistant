package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.RecipeModel
import com.luczka.mycoffee.domain.repositories.FirebaseRepository

class GetRecipesUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend operator fun invoke(methodId: String): Result<List<RecipeModel>> {
        return firebaseRepository.getRecipes(methodId)
    }
}