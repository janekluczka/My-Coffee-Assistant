package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository

class DeleteBrewUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(brewModel: BrewModel) {
        return myCoffeeDatabaseRepository.deleteBrew(brewModel)
    }
}