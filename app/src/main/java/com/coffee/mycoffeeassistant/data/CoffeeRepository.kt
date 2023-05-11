package com.coffee.mycoffeeassistant.data

import kotlinx.coroutines.flow.Flow

interface CoffeeRepository {

    fun getAllCoffeesStream(): Flow<List<Coffee>>

    fun getCoffeeStream(id: Int): Flow<Coffee?>

    suspend fun insertCoffee(coffee: Coffee)

    suspend fun updateCoffee(coffee: Coffee)

    suspend fun deleteCoffee(coffee: Coffee)

}