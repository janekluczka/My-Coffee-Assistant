package com.luczka.mycoffee.domain.repositories

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import kotlinx.coroutines.flow.Flow

interface MyCoffeeDatabaseRepository {

    suspend fun insertCoffee(coffeeModel: CoffeeModel)

    suspend fun getCoffee(coffeeId: Long): CoffeeModel?

    fun getCoffeeFlow(coffeeId: Long): Flow<CoffeeModel?>

    fun getAllCoffeesFlow() : Flow<List<CoffeeModel>>

    fun getRecentlyAddedCoffeesFlow(amount: Int): Flow<List<CoffeeModel>>

    fun getCurrentCoffeesStream(): Flow<List<CoffeeModel>>

    // TODO: Add updateCoffee(coffeeModel: CoffeeModel)

    suspend fun deleteCoffee(coffeeModel: CoffeeModel)


    suspend fun insertBrew(brewModel: BrewModel) // TODO: Merge into 1 function with insertBrewAndUpdateCoffeeModels

    suspend fun insertBrewAndUpdateCoffeeModels(
        brewModel: BrewModel,
        coffeeModels: List<CoffeeModel>
    )

    // TODO: Add suspend fun getBrew(brewId: Long): BrewModel?

    suspend fun getBrewFlow(brewId: Long): Flow<BrewModel?>

    suspend fun getAllBrewsFlow(): Flow<List<BrewModel>>

    suspend fun getRecentBrewsFlow(amount: Int): Flow<List<BrewModel>>

    // TODO: Add updateBrew(brewModel: BrewModel)

    suspend fun deleteBrew(brewModel: BrewModel)



    suspend fun updateCoffeeOld(coffeeModel: CoffeeModel)

}