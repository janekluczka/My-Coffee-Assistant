package com.luczka.mycoffee.domain.repository

import com.luczka.mycoffee.data.database.BrewWithBrewedCoffees
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import kotlinx.coroutines.flow.Flow

interface MyCoffeeDatabaseRepository {

    fun getAllCoffeesStream(): Flow<List<CoffeeEntity>>

    fun getFavouriteCoffeesStream(): Flow<List<CoffeeEntity>>

    fun getCoffeeStream(coffeeId: Int): Flow<CoffeeEntity?>

    fun getCurrentCoffeesStream(): Flow<List<CoffeeEntity>>

    suspend fun getCurrentCoffees(): List<CoffeeEntity>

    fun getAmountLowerThanStream(amount: Float): Flow<List<CoffeeEntity>>

    suspend fun insertCoffee(coffeeEntity: CoffeeEntity)

    suspend fun updateCoffee(coffeeEntity: CoffeeEntity)

    suspend fun deleteCoffee(coffeeEntity: CoffeeEntity)

    suspend fun getBrewsWithCoffees(): Flow<List<BrewWithBrewedCoffees>>

    suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewWithBrewedCoffees?>

    suspend fun insertBrew(brewEntity: BrewEntity): Long

    suspend fun insertBrewedCoffee(brewedCoffeeEntity: BrewedCoffeeEntity)

    suspend fun deleteBrew(brewEntity: BrewEntity)

}