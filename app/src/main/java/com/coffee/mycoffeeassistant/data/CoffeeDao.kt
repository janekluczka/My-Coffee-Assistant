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

    @Query("SELECT * from coffee_table ORDER BY id DESC")
    fun getCoffees(): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE isFavourite = 1 ORDER BY id DESC")
    fun getFavouriteCoffees(): Flow<List<Coffee>>

    @Query("SELECT * from coffee_table WHERE currentAmount < :amount ORDER BY currentAmount ASC")
    fun getAmountLowerThan(amount: Float): Flow<List<Coffee>>

    // TODO: Change query
    @Query("SELECT * from coffee_table WHERE roastingDate < :date ORDER BY currentAmount ASC")
    fun getOlderThan(date: String): Flow<List<Coffee>>

    @Delete
    suspend fun delete(coffee: Coffee)
}