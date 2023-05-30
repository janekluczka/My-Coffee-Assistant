package com.coffee.mycoffeeassistant.data

import kotlinx.coroutines.flow.Flow

class OfflineCoffeeRepository(private val coffeeDao: CoffeeDao) : CoffeeRepository {

    override fun getAllCoffeesStream(): Flow<List<Coffee>> =
        coffeeDao.getCoffees()

    override fun getFavouriteCoffeesStream(): Flow<List<Coffee>> =
        coffeeDao.getFavouriteCoffees()

    override fun getCoffeeStream(id: Int): Flow<Coffee?> =
        coffeeDao.getCoffee(id)

    override fun getAmountLowerThanStream(amount: Float): Flow<List<Coffee>> =
        coffeeDao.getAmountLowerThan(amount)

    override fun getOlderThanStream(date: String): Flow<List<Coffee>> =
        coffeeDao.getOlderThan(date)

    override suspend fun insertCoffee(coffee: Coffee) =
        coffeeDao.insert(coffee)

    override suspend fun updateCoffee(coffee: Coffee) =
        coffeeDao.update(coffee)

    override suspend fun deleteCoffee(coffee: Coffee) =
        coffeeDao.delete(coffee)

}