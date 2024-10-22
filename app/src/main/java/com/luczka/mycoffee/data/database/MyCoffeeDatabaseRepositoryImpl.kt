package com.luczka.mycoffee.data.database

import com.luczka.mycoffee.domain.mappers.toEntity
import com.luczka.mycoffee.domain.mappers.toModel
import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyCoffeeDatabaseRepositoryImpl(
    private val myCoffeeDao: MyCoffeeDao
) : MyCoffeeDatabaseRepository {

    override fun getAllCoffeesStream(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getAllCoffeesStream()
            .map { coffeeEntities -> coffeeEntities.map { it.toModel() } }
    }

    override fun getFavouriteCoffeesStream(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getFavouriteCoffees()
            .map { coffeeEntities -> coffeeEntities.map { it.toModel() } }
    }

    override fun getCoffeeStream(coffeeId: Int): Flow<CoffeeModel?> {
        return myCoffeeDao.getCoffee(coffeeId)
            .map { coffeeEntity -> coffeeEntity?.toModel() }
    }

    override fun getCurrentCoffeesStream(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getCurrentCoffeesStream()
            .map { coffeeEntities -> coffeeEntities.map { it.toModel() } }
    }

    override suspend fun getCurrentCoffees(): List<CoffeeModel> {
        return myCoffeeDao.getCurrentCoffees()
            .map { it.toModel() }
    }

    override fun getAmountLowerThanStream(amount: Float): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getBelowAmountCoffees(amount)
            .map { coffeeEntities -> coffeeEntities.map { it.toModel() } }
    }

    override suspend fun insertCoffee(coffeeModel: CoffeeModel) {
        return myCoffeeDao.insert(coffeeModel.toEntity())
    }

    override suspend fun updateCoffee(coffeeModel: CoffeeModel) {
        return myCoffeeDao.update(coffeeModel.toEntity())
    }

    override suspend fun deleteCoffee(coffeeModel: CoffeeModel) {
        return myCoffeeDao.delete(coffeeModel.toEntity())
    }

    override suspend fun getBrewsWithCoffees(): Flow<List<BrewModel>> {
        return myCoffeeDao.getBrewsWithCoffees()
            .map { brewsWithBrewedCoffees -> brewsWithBrewedCoffees.map { it.toModel() } }
    }

    override suspend fun getBrewWithCoffees(brewId: Int): Flow<BrewModel> {
        return myCoffeeDao.getBrewWithCoffees(brewId)
            .map { brewWithBrewedCoffees -> brewWithBrewedCoffees.toModel() }
    }

    override suspend fun insertBrew(brewModel: BrewModel) {
        myCoffeeDao.insertBrewWithBrewedCoffees(
            brewEntity = brewModel.toEntity(),
            brewedCoffeeEntities = brewModel.brewedCoffees.map { it.toEntity() }
        )
        // TODO: Update ammounts
    }

    override suspend fun insertBrewAndUpdateCoffeeModels(
        brewModel: BrewModel,
        coffeeModels: List<CoffeeModel>
    ) {
        myCoffeeDao.insertBrewWithBrewedCoffees(
            brewEntity = brewModel.toEntity(),
            brewedCoffeeEntities = brewModel.brewedCoffees.map { it.toEntity() }
        )
        // TODO: Update ammounts
    }

    override suspend fun deleteBrew(brewModel: BrewModel) {
        return myCoffeeDao.delete(brewModel.toEntity())
    }

}