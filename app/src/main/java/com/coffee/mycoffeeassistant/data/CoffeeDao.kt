package com.coffee.mycoffeeassistant.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDao {
    @Insert
    suspend fun insert(coffee: Coffee)

    @Update
    suspend fun update(coffee: Coffee)

    @Query("SELECT * from coffee_table WHERE id = :id")
    fun getCoffee(id: Int): Flow<Coffee>

    @Query("SELECT * from coffee_table ORDER BY id ASC")
    fun getCoffees(): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE isFavourite = 1 ORDER BY id DESC")
    fun getFavouriteCoffees(): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE amount > 0 ORDER BY id ASC")
    fun getInStockCoffees(): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE amount < :amount AND amount > 0 ORDER BY amount ASC")
    fun getAmountLowerThan(amount: Float): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE roastingDate < :date AND amount > 0 ORDER BY amount ASC")
    fun getOlderThan(date: String): Flow<List<Coffee>>

    @Delete
    suspend fun delete(coffee: Coffee)
}