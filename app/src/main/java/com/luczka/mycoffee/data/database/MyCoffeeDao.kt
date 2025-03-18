package com.luczka.mycoffee.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.luczka.mycoffee.data.database.entities.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeImageEntity
import com.luczka.mycoffee.data.database.queries.BrewWithBrewedCoffeesRelation
import com.luczka.mycoffee.data.database.queries.CoffeeWithCoffeeImagesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface MyCoffeeDao {

    @Transaction
    suspend fun insertCoffeeWithImages(
        coffeeEntity: CoffeeEntity,
        coffeeImageEntities: List<CoffeeImageEntity>
    ): Long {
        val coffeeId = insertCoffee(coffeeEntity)
        val coffeeImageEntitiesWithCoffeeId = coffeeImageEntities.map { it.copy(coffeeId = coffeeId) }
        insertCoffeeImages(coffeeImageEntitiesWithCoffeeId)
        return coffeeId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoffee(coffee: CoffeeEntity): Long

    @Insert
    suspend fun insertCoffeeImages(coffeeImageEntities: List<CoffeeImageEntity>)

    @Transaction
    @Query("SELECT * FROM CoffeeEntity WHERE coffeeId = :coffeeId")
    suspend fun getCoffeeWithImages(coffeeId: Long): CoffeeWithCoffeeImagesRelation?

    @Transaction
    @Query("SELECT * FROM CoffeeEntity WHERE coffeeId = :coffeeId")
    fun getCoffeeWithImagesFlow(coffeeId: Long): Flow<CoffeeWithCoffeeImagesRelation?>

    @Transaction
    @Query("SELECT * FROM CoffeeEntity ORDER BY addedOn, coffeeId DESC")
    fun getAllCoffeesWithImagesFlow(): Flow<List<CoffeeWithCoffeeImagesRelation>>

    @Transaction
    @Query("SELECT * FROM CoffeeEntity ORDER BY addedOn, coffeeId DESC LIMIT :amount")
    fun getRecentlyAddedCoffeesWithImagesFlow(amount: Int): Flow<List<CoffeeWithCoffeeImagesRelation>>

    @Transaction
    @Query("SELECT * FROM CoffeeEntity WHERE amount > 0 ORDER BY name, brand, coffeeId ASC")
    fun getCurrentCoffeesWithImagesFlow(): Flow<List<CoffeeWithCoffeeImagesRelation>>

    @Transaction
    @Query("SELECT * FROM CoffeeEntity WHERE amount > 0 ORDER BY name, brand, coffeeId ASC")
    suspend fun getCurrentCoffeesWithImages(): List<CoffeeWithCoffeeImagesRelation>

    @Update
    suspend fun updateCoffee(coffeeEntity: CoffeeEntity)

    @Delete
    suspend fun deleteCoffee(coffeeEntity: CoffeeEntity)


    @Transaction
    suspend fun insertBrewWithBrewedCoffees(
        brewEntity: BrewEntity,
        brewCoffeeCrossRefs: List<BrewCoffeeCrossRef>
    ): Long {
        val brewId = insertBrew(brewEntity)
        val brewCoffeeCrossRefsWithBrewId = brewCoffeeCrossRefs.map { it.copy(brewId = brewId) }
        insertBrewedCoffees(brewCoffeeCrossRefsWithBrewId)
        return brewId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrew(brewEntity: BrewEntity): Long

    @Insert
    suspend fun insertBrewedCoffees(brewCoffeeCrossRefs: List<BrewCoffeeCrossRef>)

    @Transaction
    @Query("SELECT * FROM BrewEntity WHERE brewId = :brewId")
    fun getBrewWithCoffeesFlow(brewId: Long): Flow<BrewWithBrewedCoffeesRelation>

    @Transaction
    @Query("SELECT * FROM BrewEntity ORDER BY date, brewId DESC")
    fun getBrewsWithCoffeesFlow(): Flow<List<BrewWithBrewedCoffeesRelation>>

    @Transaction
    @Query("SELECT * FROM BrewEntity ORDER BY date, brewId DESC LIMIT :amount")
    fun getRecentBrewsWithCoffeesFlow(amount: Int): Flow<List<BrewWithBrewedCoffeesRelation>>

    @Delete
    suspend fun deleteBrew(brewEntity: BrewEntity)

}