package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class GetBrewFlowUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(brewId: Long): Flow<BrewModel?> {
        return myCoffeeDatabaseRepository.getBrewFlow(brewId)
    }
}