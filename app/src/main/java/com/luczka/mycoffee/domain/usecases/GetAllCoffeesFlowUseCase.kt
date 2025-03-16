package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetAllCoffeesFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    operator fun invoke(): Flow<List<CoffeeModel>> {
        return myCoffeeDatabaseRepository.getAllCoffeesFlow()
    }
}