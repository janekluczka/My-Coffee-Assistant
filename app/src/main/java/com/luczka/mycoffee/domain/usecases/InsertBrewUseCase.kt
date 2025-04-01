package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository

class InsertBrewUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(brewModel: BrewModel): Long {
        return myCoffeeDatabaseRepository.insertBrew(brewModel)
    }
}