package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetCoffeeFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    operator fun invoke(coffeeId: Long): Flow<CoffeeModel?> {
        return myCoffeeDatabaseRepository.getCoffeeFlow(coffeeId)
    }
}