package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyAddedBrewsFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(amount: Int): Flow<List<BrewModel>> {
        return myCoffeeDatabaseRepository.getRecentBrewsFlow(amount)
    }
}