package com.luczka.mycoffee.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyCoffeeDao {
    /**
     * Insert a coffee into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coffeeEntity: CoffeeEntity)

    /**
     * Update a coffee in the database.
     */
    @Update
    suspend fun update(coffeeEntity: CoffeeEntity)

    /**
     * Get a coffee by its ID.
     * @param id The ID of the coffee.
     * @return A [Flow] emitting the coffee, or null if not found.
     */
    @Query("SELECT * FROM coffeeentity WHERE coffeeId = :id")
    fun getCoffee(id: Int): Flow<CoffeeEntity?>

    /**
     * Get all coffees in ascending order of coffeeId.
     * @return A [Flow] emitting a list of all coffees.
     */
    @Query("SELECT * FROM coffeeentity ORDER BY coffeeId ASC")
    fun getAllCoffeesStream(): Flow<List<CoffeeEntity>>

    /**
     * Get favorite coffees in descending order of coffeeId.
     * @return A [Flow] emitting a list of favorite coffees.
     */
    @Query("SELECT * FROM coffeeentity WHERE isFavourite = 1 ORDER BY coffeeId DESC")
    fun getFavouriteCoffees(): Flow<List<CoffeeEntity>>

    /**
     * Get current coffees (amount > 0) in ascending order of coffeeId.
     * @return A [Flow] emitting a list of current coffees.
     */
    @Query("SELECT * FROM coffeeentity WHERE amount > 0 ORDER BY coffeeId ASC")
    fun getCurrentCoffeesStream(): Flow<List<CoffeeEntity>>

    @Query("SELECT * FROM coffeeentity WHERE amount > 0 ORDER BY coffeeId ASC")
    fun getCurrentCoffees(): List<CoffeeEntity>

    /**
     * Get current coffees (amount > 0) with an amount less than the specified amount (amount < [amount]),
     * ordered by amount in ascending order.
     * @param amount The maximum amount to filter by.
     * @return A [Flow] emitting a list of coffees below the specified amount.
     */
    @Query("SELECT * FROM coffeeentity WHERE amount < :amount AND amount > 0 ORDER BY amount ASC")
    fun getBelowAmountCoffees(amount: Float): Flow<List<CoffeeEntity>>

    /**
     * Delete a coffee from the database.
     */
    @Delete
    suspend fun delete(coffeeEntity: CoffeeEntity)

    @Transaction
    @Query("SELECT * FROM BrewEntity")
    fun getBrewsWithCoffees(): Flow<List<BrewWithBrewedCoffees>>

    @Transaction
    @Query("SELECT * FROM BrewEntity WHERE brewId = :id")
    fun getBrewWithCoffees(id: Int): Flow<BrewWithBrewedCoffees>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrew(brewEntity: BrewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewedCoffee(brewedCoffeeEntity: BrewedCoffeeEntity)

    @Delete
    suspend fun delete(brewEntity: BrewEntity)

}