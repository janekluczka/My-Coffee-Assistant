package com.coffee.mycoffeeassistant.data

import kotlinx.coroutines.flow.Flow

class OfflineCoffeeRepository(private val coffeeDao: CoffeeDao): CoffeeRepository {

    override fun getAllCoffeesStream(): Flow<List<Coffee>> = coffeeDao.getCoffees()

    override fun getCoffeeStream(id: Int): Flow<Coffee?> = coffeeDao.getCoffee(id)

    override suspend fun insertCoffee(coffee: Coffee) = coffeeDao.insert(coffee)

    override suspend fun updateCoffee(coffee: Coffee) = coffeeDao.update(coffee)

    override suspend fun deleteCoffee(coffee: Coffee) = coffeeDao.delete(coffee)

}