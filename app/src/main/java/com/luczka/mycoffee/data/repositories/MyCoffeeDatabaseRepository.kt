package com.luczka.mycoffee.data.repositories

import com.luczka.mycoffee.data.database.BrewWithBrewedCoffees
import com.luczka.mycoffee.data.database.entities.Brew
import com.luczka.mycoffee.data.database.entities.BrewedCoffee
import com.luczka.mycoffee.data.database.entities.Coffee
import kotlinx.coroutines.flow.Flow

interface MyCoffeeDatabaseRepository {

    fun getAllCoffeesStream(): Flow<List<Coffee>>

    fun getFavouriteCoffeesStream(): Flow<List<Coffee>>

    fun getCoffeeStream(coffeeId: Int): Flow<Coffee?>

    fun getCurrentCoffeesStream(): Flow<List<Coffee>>

    suspend fun getCurrentCoffees(): List<Coffee>

    fun getAmountLowerThanStream(amount: Float): Flow<List<Coffee>>

    fun getOlderThanStream(date: String): Flow<List<Coffee>>

    suspend fun insertCoffee(coffee: Coffee)

    suspend fun updateCoffee(coffee: Coffee)

    suspend fun deleteCoffee(coffee: Coffee)

    suspend fun getBrewsWithCoffees(): Flow<List<BrewWithBrewedCoffees>>

    suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewWithBrewedCoffees>

    suspend fun insertBrew(brew: Brew): Long

    suspend fun insertBrewedCoffee(brewedCoffee: BrewedCoffee)

    suspend fun deleteBrew(brew: Brew)

}