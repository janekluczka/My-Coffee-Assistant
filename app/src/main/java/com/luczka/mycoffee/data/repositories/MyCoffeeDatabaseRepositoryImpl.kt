package com.luczka.mycoffee.data.repositories

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.luczka.mycoffee.data.database.MyCoffeeDao
import com.luczka.mycoffee.data.mappers.toEntity
import com.luczka.mycoffee.data.mappers.toModel
import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class MyCoffeeDatabaseRepositoryImpl(
    private val context: Context,
    private val myCoffeeDao: MyCoffeeDao
) : MyCoffeeDatabaseRepository {

    override suspend fun insertCoffee(coffeeModel: CoffeeModel) {
        val contentResolver = context.contentResolver
        val filesDir = context.filesDir

        val coffeeImageEntitiesWithImageFilename = coffeeModel.coffeeImages.map { coffeeImage ->
            val fileName = getFileNameFromContentResolver(contentResolver, coffeeImage.uri) ?: throw IllegalArgumentException("Cannot determine filename for ${coffeeImage.uri}")
            val destinationFile = File(filesDir, fileName)

            if (!destinationFile.exists()) {
                val inputStream = contentResolver.openInputStream(coffeeImage.uri) ?: throw IllegalArgumentException("Cannot open InputStream for ${coffeeImage.uri}")

                inputStream.use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            coffeeImage.copy(filename = destinationFile.name).toEntity()
        }

        myCoffeeDao.insertCoffeeWithImages(
            coffeeEntity = coffeeModel.toEntity(),
            coffeeImageEntities = coffeeImageEntitiesWithImageFilename
        )
    }

    private fun getFileNameFromContentResolver(contentResolver: ContentResolver, uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return it.getString(displayNameIndex)
                }
            }
        }
        return null
    }

    override suspend fun getCoffee(coffeeId: Long): CoffeeModel? {
        return myCoffeeDao.getCoffeeWithImages(coffeeId)?.toModel()
    }

    override fun getCoffeeFlow(coffeeId: Long): Flow<CoffeeModel?> {
        return myCoffeeDao.getCoffeeWithImagesFlow(coffeeId)
            .map { coffeeWithCoffeeImagesRelation ->
                coffeeWithCoffeeImagesRelation?.toModel()
            }
    }

    override fun getAllCoffeesFlow(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getAllCoffeesWithImagesFlow()
            .map { coffeeWithCoffeeImagesRelations ->
                coffeeWithCoffeeImagesRelations.map { it.toModel() }
            }
    }

    override fun getRecentlyAddedCoffeesFlow(amount: Int): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getRecentlyAddedCoffeesWithImagesFlow(amount)
            .map { coffeeWithCoffeeImagesRelations ->
                coffeeWithCoffeeImagesRelations.map { it.toModel() }
            }
    }

    override fun getCurrentCoffeesStream(): Flow<List<CoffeeModel>> {
        return myCoffeeDao.getCurrentCoffeesWithImagesFlow()
            .map { coffeeWithCoffeeImagesRelations ->
                coffeeWithCoffeeImagesRelations.map { it.toModel() }
            }
    }

    override suspend fun deleteCoffee(coffeeModel: CoffeeModel) {
        val filesDir = context.filesDir

        val coffeeImageFiles = coffeeModel.coffeeImages.map { File(filesDir, it.filename) }

        myCoffeeDao.delete(coffeeEntity = coffeeModel.toEntity())

        coffeeImageFiles.forEach { imageFile ->
            if (imageFile.exists()) {
                imageFile.delete()
            }
        }

    }

    override suspend fun updateCoffeeOld(coffeeModel: CoffeeModel) {
        return myCoffeeDao.update(coffeeModel.toEntity())
    }

    override suspend fun deleteCoffeeOld(coffeeModel: CoffeeModel) {
        return myCoffeeDao.delete(coffeeModel.toEntity())
    }

    override suspend fun getAllBrewsFlow(): Flow<List<BrewModel>> {
        return myCoffeeDao.getBrewsWithCoffeesFlow()
            .map { brewsWithBrewedCoffees -> brewsWithBrewedCoffees.map { it.toModel() } }
    }

    override suspend fun getRecentBrewsFlow(amount: Int): Flow<List<BrewModel>> {
        return myCoffeeDao.getRecentBrewsWithCoffeesFlow(amount)
            .map { brewsWithBrewedCoffees -> brewsWithBrewedCoffees.map { it.toModel() } }
    }

    override suspend fun getBrewFlow(brewId: Long): Flow<BrewModel> {
        return myCoffeeDao.getBrewWithCoffeesFlow(brewId)
            .map { brewWithBrewedCoffees -> brewWithBrewedCoffees.toModel() }
    }

    override suspend fun insertBrew(brewModel: BrewModel) {
        myCoffeeDao.insertBrewWithBrewedCoffees(
            brewEntity = brewModel.toEntity(),
            brewCoffeeCrossRefs = brewModel.brewedCoffees.map { it.toEntity() }
        )
        // TODO: Update ammounts
    }

    override suspend fun insertBrewAndUpdateCoffeeModels(
        brewModel: BrewModel,
        coffeeModels: List<CoffeeModel>
    ) {
        myCoffeeDao.insertBrewWithBrewedCoffees(
            brewEntity = brewModel.toEntity(),
            brewCoffeeCrossRefs = brewModel.brewedCoffees.map { it.toEntity() }
        )
        // TODO: Update ammounts
    }

    override suspend fun deleteBrew(brewModel: BrewModel) {
        return myCoffeeDao.delete(brewModel.toEntity())
    }

}