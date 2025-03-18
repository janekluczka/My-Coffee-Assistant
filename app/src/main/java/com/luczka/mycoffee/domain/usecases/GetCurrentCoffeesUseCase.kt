package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository

class GetCurrentCoffeesUseCase(private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository) {

    suspend operator fun invoke(): List<CoffeeModel> {
        return myCoffeeDatabaseRepository.getCurrentCoffees()
    }
}