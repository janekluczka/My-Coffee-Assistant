package com.coffee.mycoffeeassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Coffee::class],
    version = 1,
    exportSchema = false
)
abstract class CoffeeDatabase : RoomDatabase() {

    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var Instance: CoffeeDatabase? = null

        fun getDatabase(context: Context): CoffeeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CoffeeDatabase::class.java, "coffee_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}