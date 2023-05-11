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

    @Delete
    suspend fun delete(coffee: Coffee)
}