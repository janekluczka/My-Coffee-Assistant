package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository

class UpdateCoffeeUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(coffeeModel: CoffeeModel) {
        return myCoffeeDatabaseRepository.updateCoffee(coffeeModel)
    }
}