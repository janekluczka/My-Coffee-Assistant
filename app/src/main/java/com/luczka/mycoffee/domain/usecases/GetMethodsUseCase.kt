package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CategoryModel
import com.luczka.mycoffee.domain.repositories.FirebaseRepository

class GetMethodsUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend operator fun invoke(): Result<List<CategoryModel>> {
        return firebaseRepository.getCategories()
    }
}