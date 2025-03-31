package com.luczka.mycoffee.domain.repositories

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import kotlinx.coroutines.flow.Flow

interface MyCoffeeDatabaseRepository {

    suspend fun insertCoffee(coffeeModel: CoffeeModel): Long

    suspend fun getCoffee(coffeeId: Long): CoffeeModel?

    fun getCoffeeFlow(coffeeId: Long): Flow<CoffeeModel?>

    fun getAllCoffeesFlow() : Flow<List<CoffeeModel>>

    suspend fun getAllCoffees() : List<CoffeeModel>

    fun getFavouriteCoffeesFlow(): Flow<List<CoffeeModel>>

    fun getRecentlyAddedCoffeesFlow(amount: Int): Flow<List<CoffeeModel>>

    suspend fun updateCoffee(coffeeModel: CoffeeModel)

    suspend fun updateCoffeeWithPhotos(oldCoffee: CoffeeModel, updatedCoffee: CoffeeModel)

    suspend fun deleteCoffee(coffeeModel: CoffeeModel)

    suspend fun insertBrew(brewModel: BrewModel) : Long

    // TODO: Add suspend fun getBrew(brewId: Long): BrewModel?

    suspend fun getBrewFlow(brewId: Long): Flow<BrewModel?>

    suspend fun getAllBrewsFlow(): Flow<List<BrewModel>>

    suspend fun getRecentBrewsFlow(amount: Int): Flow<List<BrewModel>>

    // TODO: Add updateBrew(brewModel: BrewModel)

    suspend fun deleteBrew(brewModel: BrewModel)

}