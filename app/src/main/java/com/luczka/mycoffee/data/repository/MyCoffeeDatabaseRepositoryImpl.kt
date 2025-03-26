package com.luczka.mycoffee.data.repository

import com.luczka.mycoffee.data.local.ImageManager
import com.luczka.mycoffee.data.local.MyCoffeeDao
import com.luczka.mycoffee.data.mapper.toEntity
import com.luczka.mycoffee.data.mapper.toModel
import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyCoffeeDatabaseRepositoryImpl(
    private val myCoffeeDao: MyCoffeeDao,
    private val imageManager: ImageManager
) : MyCoffeeDatabaseRepository {

    override suspend fun insertCoffee(coffeeModel: CoffeeModel): Long {
        val coffeeImageEntitiesWithImageFilename = coffeeModel.coffeeImages.map { coffeeImage ->
            val fileName = imageManager.saveImageToInternalStorage(coffeeImage.uri) ?: throw IllegalArgumentException("Cannot save image for ${coffeeImage.uri}")
            coffeeImage.copy(filename = fileName).toEntity()
        }

        return myCoffeeDao.insertCoffeeWithImages(
            coffeeEntity = coffeeModel.toEntity(),
            coffeeImageEntities = coffeeImageEntitiesWithImageFilename
        )
    }

    override suspend fun getCoffee(coffeeId: Long): CoffeeModel? {
        return myCoffeeDao.getCoffeeWithImages(coffeeId)?.toModel()
    }

    override fun getCoffeeFlow(coffeeId: Long): Flow<CoffeeModel?> {
        return myCoffeeDao.getCoffeeWithImagesFlow(coffeeId)
            .map { it?.toModel() }
    }

    override fun getAllCoffeesFlow(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getAllCoffeesWithImagesFlow()
            .map { it.toModel() }
    }

    override fun getRecentlyAddedCoffeesFlow(amount: Int): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getRecentlyAddedCoffeesWithImagesFlow(amount)
            .map { it.toModel() }
    }

    override fun getCurrentCoffeesFlow(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getCurrentCoffeesWithImagesFlow()
            .map { it.toModel() }
    }

    override suspend fun getCurrentCoffees(): List<CoffeeModel> {
        return myCoffeeDao.getCurrentCoffeesWithImages().toModel()
    }

    override suspend fun updateCoffee(coffeeModel: CoffeeModel) {
        return myCoffeeDao.updateCoffee(coffeeModel.toEntity())
    }

    override suspend fun updateCoffeeWithPhotos(oldCoffee: CoffeeModel, updatedCoffee: CoffeeModel) {
        TODO("Not yet implemented")
        // Chceck difference between image files, add new, remove deleted and update those which place changed.
    }

    override suspend fun deleteCoffee(coffeeModel: CoffeeModel) {
        coffeeModel.coffeeImages.forEach { coffeeImageModel ->
            imageManager.deleteImageFromInternalStorage(coffeeImageModel.filename)
        }
        myCoffeeDao.deleteCoffee(coffeeEntity = coffeeModel.toEntity())
    }

    override suspend fun insertBrewAndUpdateCoffeeModels(
        brewModel: BrewModel,
        coffeeModels: List<CoffeeModel>
    ): Long {
        val brewId = myCoffeeDao.insertBrewWithBrewedCoffees(
            brewEntity = brewModel.toEntity(),
            brewCoffeeCrossRefs = brewModel.brewedCoffees.map { it.toEntity() }
        )
        // TODO: Update ammounts

        return brewId
    }

    override suspend fun getAllBrewsFlow(): Flow<List<BrewModel>> {
        return myCoffeeDao.getBrewsWithCoffeesFlow()
            .map { it.toModel() }
    }

    override suspend fun getRecentBrewsFlow(amount: Int): Flow<List<BrewModel>> {
        return myCoffeeDao.getRecentBrewsWithCoffeesFlow(amount)
            .map { it.toModel() }
    }

    override suspend fun getBrewFlow(brewId: Long): Flow<BrewModel> {
        return myCoffeeDao.getBrewWithCoffeesFlow(brewId)
            .map { it.toModel() }
    }

    override suspend fun deleteBrew(brewModel: BrewModel) {
        return myCoffeeDao.deleteBrew(brewModel.toEntity())
    }
}