package com.luczka.mycoffee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luczka.mycoffee.data.database.converters.CoffeeMachineTypeConverter
import com.luczka.mycoffee.data.database.converters.GrinderTypeConverter
import com.luczka.mycoffee.data.database.converters.LocalDateConverter
import com.luczka.mycoffee.data.database.converters.ProcessTypeConverter
import com.luczka.mycoffee.data.database.converters.RoastTypeConverter
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.data.database.entities.EquipmentEntity
import com.luczka.mycoffee.data.database.entities.ReceiptEntity

@Database(
    entities = [
        BrewedCoffeeEntity::class,
        BrewEntity::class,
        CoffeeEntity::class,
        EquipmentEntity.CoffeeMachineEntity::class,
        EquipmentEntity.GrinderEntity::class,
        ReceiptEntity::class
    ],
    version = 29,
    exportSchema = false
)
@TypeConverters(
    CoffeeMachineTypeConverter::class,
    GrinderTypeConverter::class,
    LocalDateConverter::class,
    ProcessTypeConverter::class,
    RoastTypeConverter::class
)
abstract class MyCoffeeDatabase : RoomDatabase() {

    abstract fun myCoffeeDao(): MyCoffeeDao

}