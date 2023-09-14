package com.luczka.mycoffee.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDao {
    /**
     * Insert a coffee into the database.
     */
    @Insert
    suspend fun insert(coffee: Coffee)

    /**
     * Update a coffee in the database.
     */
    @Update
    suspend fun update(coffee: Coffee)

    /**
     * Get a coffee by its ID.
     * @param id The ID of the coffee.
     * @return A [Flow] emitting the coffee, or null if not found.
     */
    @Query("SELECT * FROM coffee_table WHERE id = :id")
    fun getCoffee(id: Int): Flow<Coffee?>

    /**
     * Get all coffees in ascending order of ID.
     * @return A [Flow] emitting a list of all coffees.
     */
    @Query("SELECT * FROM coffee_table ORDER BY id ASC")
    fun getAllCoffees(): Flow<List<Coffee>>

    /**
     * Get favorite coffees in descending order of ID.
     * @return A [Flow] emitting a list of favorite coffees.
     */
    @Query("SELECT * FROM coffee_table WHERE isFavourite = 1 ORDER BY id DESC")
    fun getFavouriteCoffees(): Flow<List<Coffee>>

    /**
     * Get current coffees (amount > 0) in ascending order of ID.
     * @return A [Flow] emitting a list of current coffees.
     */
    @Query("SELECT * FROM coffee_table WHERE amount > 0 ORDER BY id ASC")
    fun getCurrentCoffees(): Flow<List<Coffee>>

    /**
     * Get current coffees (amount > 0) with an amount less than the specified amount (amount < [amount]),
     * ordered by amount in ascending order.
     * @param amount The maximum amount to filter by.
     * @return A [Flow] emitting a list of coffees below the specified amount.
     */
    @Query("SELECT * FROM coffee_table WHERE amount < :amount AND amount > 0 ORDER BY amount ASC")
    fun getBelowAmountCoffees(amount: Float): Flow<List<Coffee>>

    /**
     * Get current coffees (amount > 0) with a roasting date older than the specified date,
     * ordered by amount in ascending order.
     * @param date The date to compare against.
     * @return A [Flow] emitting a list of coffees with roasting date older than the specified date.
     */
    @Query("SELECT * FROM coffee_table WHERE roastingDate < :date AND amount > 0 ORDER BY amount ASC")
    fun getOlderThanCoffees(date: String): Flow<List<Coffee>>

    /**
     * Delete a coffee from the database.
     */
    @Delete
    suspend fun delete(coffee: Coffee)
}