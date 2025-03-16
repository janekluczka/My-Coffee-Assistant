package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyAddedCoffeesFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    operator fun invoke(amount: Int): Flow<List<CoffeeModel>> {
        return myCoffeeDatabaseRepository.getRecentlyAddedCoffeesFlow(amount)
    }
}