package com.luczka.mycoffee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity

@Database(
    entities = [
        CoffeeEntity::class,
        BrewEntity::class,
        BrewedCoffeeEntity::class
    ],
    version = 27,
    exportSchema = false
)
abstract class MyCoffeeDatabase : RoomDatabase() {

    abstract fun myCoffeeDao(): MyCoffeeDao

}