package com.luczka.mycoffee.data.repository

import com.luczka.mycoffee.data.database.BrewWithBrewedCoffees
import com.luczka.mycoffee.data.database.MyCoffeeDao
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow

class MyCoffeeDatabaseRepositoryImpl(
    private val myCoffeeDao: MyCoffeeDao
) : MyCoffeeDatabaseRepository {

    override fun getAllCoffeesStream(): Flow<List<CoffeeEntity>> =
        myCoffeeDao.getAllCoffeesStream()

    override fun getFavouriteCoffeesStream(): Flow<List<CoffeeEntity>> =
        myCoffeeDao.getFavouriteCoffees()

    override fun getCoffeeStream(coffeeId: Int): Flow<CoffeeEntity?> =
        myCoffeeDao.getCoffee(coffeeId)

    override fun getCurrentCoffeesStream(): Flow<List<CoffeeEntity>> =
        myCoffeeDao.getCurrentCoffeesStream()

    override suspend fun getCurrentCoffees(): List<CoffeeEntity> =
        myCoffeeDao.getCurrentCoffees()

    override fun getAmountLowerThanStream(amount: Float): Flow<List<CoffeeEntity>> =
        myCoffeeDao.getBelowAmountCoffees(amount)

    override suspend fun insertCoffee(coffeeEntity: CoffeeEntity) =
        myCoffeeDao.insert(coffeeEntity)

    override suspend fun updateCoffee(coffeeEntity: CoffeeEntity) =
        myCoffeeDao.update(coffeeEntity)

    override suspend fun deleteCoffee(coffeeEntity: CoffeeEntity) =
        myCoffeeDao.delete(coffeeEntity)

    override suspend fun getBrewsWithCoffees(): Flow<List<BrewWithBrewedCoffees>> =
        myCoffeeDao.getBrewsWithCoffees()

    override suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewWithBrewedCoffees> =
        myCoffeeDao.getBrewWithCoffees(brewId)

    override suspend fun insertBrew(brewEntity: BrewEntity) =
        myCoffeeDao.insertBrew(brewEntity)

    override suspend fun insertBrewedCoffee(brewedCoffeeEntity: BrewedCoffeeEntity) =
        myCoffeeDao.insertBrewedCoffee(brewedCoffeeEntity)

    override suspend fun deleteBrew(brewEntity: BrewEntity) =
        myCoffeeDao.delete(brewEntity)

}