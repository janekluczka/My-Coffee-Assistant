package com.luczka.mycoffee.data.repositories

import com.luczka.mycoffee.data.database.BrewWithBrewedCoffees
import com.luczka.mycoffee.data.database.MyCoffeeDao
import com.luczka.mycoffee.data.database.entities.Brew
import com.luczka.mycoffee.data.database.entities.BrewedCoffee
import com.luczka.mycoffee.data.database.entities.Coffee
import kotlinx.coroutines.flow.Flow

class OfflineMyCoffeeDatabaseRepository(
    private val myCoffeeDao: MyCoffeeDao
) : MyCoffeeDatabaseRepository {

    override fun getAllCoffeesStream(): Flow<List<Coffee>> =
        myCoffeeDao.getAllCoffeesStream()

    override fun getFavouriteCoffeesStream(): Flow<List<Coffee>> =
        myCoffeeDao.getFavouriteCoffees()

    override fun getCoffeeStream(coffeeId: Int): Flow<Coffee?> =
        myCoffeeDao.getCoffee(coffeeId)

    override fun getCurrentCoffeesStream(): Flow<List<Coffee>> =
        myCoffeeDao.getCurrentCoffeesStream()

    override suspend fun getCurrentCoffees(): List<Coffee> =
        myCoffeeDao.getCurrentCoffees()

    override fun getAmountLowerThanStream(amount: Float): Flow<List<Coffee>> =
        myCoffeeDao.getBelowAmountCoffees(amount)

    override fun getOlderThanStream(date: String): Flow<List<Coffee>> =
        myCoffeeDao.getOlderThanCoffees(date)

    override suspend fun insertCoffee(coffee: Coffee) =
        myCoffeeDao.insert(coffee)

    override suspend fun updateCoffee(coffee: Coffee) =
        myCoffeeDao.update(coffee)

    override suspend fun deleteCoffee(coffee: Coffee) =
        myCoffeeDao.delete(coffee)

    override suspend fun getBrewsWithCoffees(): Flow<List<BrewWithBrewedCoffees>> =
        myCoffeeDao.getBrewsWithCoffees()

    override suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewWithBrewedCoffees> =
        myCoffeeDao.getBrewWithCoffees(brewId)

    override suspend fun insertBrew(brew: Brew) =
        myCoffeeDao.insertBrew(brew)

    override suspend fun insertBrewedCoffee(brewedCoffee: BrewedCoffee) =
        myCoffeeDao.insertBrewedCoffee(brewedCoffee)

    override suspend fun deleteBrew(brew: Brew) {
        TODO("Not yet implemented")
    }


}