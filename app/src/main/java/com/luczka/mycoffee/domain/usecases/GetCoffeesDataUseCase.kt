package com.luczka.mycoffee.domain.usecases

import com.luczka.mycoffee.domain.models.CoffeeFilterModel
import com.luczka.mycoffee.domain.models.CoffeesDataModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCoffeesDataUseCase(private val coffeeRepository: MyCoffeeDatabaseRepository) {

    operator fun invoke(filter: CoffeeFilterModel): Flow<CoffeesDataModel> {
        val allCoffeesFlow = coffeeRepository.getAllCoffeesFlow()
        val favouriteCoffeesFlow = coffeeRepository.getFavouriteCoffeesFlow()

        val filteredCoffeesFlow = when (filter) {
            CoffeeFilterModel.All -> allCoffeesFlow
            CoffeeFilterModel.Favourites -> favouriteCoffeesFlow
        }

        return combine(allCoffeesFlow, filteredCoffeesFlow) { allCoffees, filteredCoffees ->
            CoffeesDataModel(
                hasAny = allCoffees.isNotEmpty(),
                filteredCoffees = filteredCoffees
            )
        }
    }
}