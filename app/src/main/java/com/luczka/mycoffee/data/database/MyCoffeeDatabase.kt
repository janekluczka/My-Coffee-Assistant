package com.luczka.mycoffee.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luczka.mycoffee.data.database.entities.Brew
import com.luczka.mycoffee.data.database.entities.BrewedCoffee
import com.luczka.mycoffee.data.database.entities.Coffee

@Database(
    entities = [
        Coffee::class,
        Brew::class,
        BrewedCoffee::class
    ],
    version = 26,
    exportSchema = false
)
abstract class MyCoffeeDatabase : RoomDatabase() {

    abstract fun myCoffeeDao(): MyCoffeeDao

    companion object {
        @Volatile
        private var Instance: MyCoffeeDatabase? = null

        fun getDatabase(context: Context): MyCoffeeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyCoffeeDatabase::class.java, "coffee_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}