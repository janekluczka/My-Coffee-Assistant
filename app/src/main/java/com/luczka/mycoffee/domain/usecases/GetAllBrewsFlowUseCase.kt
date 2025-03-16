package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetAllBrewsFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(): Flow<List<BrewModel>> {
        return myCoffeeDatabaseRepository.getAllBrewsFlow()
    }
}