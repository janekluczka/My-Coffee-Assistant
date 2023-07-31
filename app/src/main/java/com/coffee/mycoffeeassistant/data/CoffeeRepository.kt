package com.coffee.mycoffeeassistant.data

import kotlinx.coroutines.flow.Flow

interface CoffeeRepository {

    fun getAllCoffeesStream(): Flow<List<Coffee>>

    fun getFavouriteCoffeesStream(): Flow<List<Coffee>>

    fun getCoffeeStream(id: Int): Flow<Coffee?>

    fun getInStockCoffeesStream(): Flow<List<Coffee>>

    fun getAmountLowerThanStream(amount: Float): Flow<List<Coffee>>

    fun getOlderThanStream(date: String): Flow<List<Coffee>>

    suspend fun insertCoffee(coffee: Coffee)

    suspend fun updateCoffee(coffee: Coffee)

    suspend fun deleteCoffee(coffee: Coffee)

}