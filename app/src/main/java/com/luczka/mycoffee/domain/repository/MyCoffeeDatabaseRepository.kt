package com.luczka.mycoffee.domain.repository

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import kotlinx.coroutines.flow.Flow

interface MyCoffeeDatabaseRepository {

    fun getAllCoffeesStream(): Flow<List<CoffeeModel>>

    fun getFavouriteCoffeesStream(): Flow<List<CoffeeModel>>

    fun getCoffeeStream(coffeeId: Int): Flow<CoffeeModel?>

    fun getCurrentCoffeesStream(): Flow<List<CoffeeModel>>

    suspend fun getCurrentCoffees(): List<CoffeeModel>

    fun getAmountLowerThanStream(amount: Float): Flow<List<CoffeeModel>>

    suspend fun insertCoffee(coffeeModel: CoffeeModel)

    suspend fun updateCoffee(coffeeModel: CoffeeModel)

    suspend fun deleteCoffee(coffeeModel: CoffeeModel)

    suspend fun getBrewsWithCoffees(): Flow<List<BrewModel>>

    suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewModel?>

    suspend fun insertBrew(brewModel: BrewModel)

    suspend fun insertBrewAndUpdateCoffeeModels(
        brewModel: BrewModel,
        coffeeModels: List<CoffeeModel>
    )

    suspend fun deleteBrew(brewModel: BrewModel)

}