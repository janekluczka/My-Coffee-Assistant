package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.domain.repositories.FirebaseRepository

class GetMethodsUseCase(private val firebaseRepository: FirebaseRepository) {

    suspend operator fun invoke(): Result<List<MethodModel>> {
        return firebaseRepository.getMethods()
    }
}