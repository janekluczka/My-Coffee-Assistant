package com.luczka.mycoffee.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luczka.mycoffee.data.local.converter.CoffeeMachineTypeConverter
import com.luczka.mycoffee.data.local.converter.GrinderTypeConverter
import com.luczka.mycoffee.data.local.converter.LocalDateConverter
import com.luczka.mycoffee.data.local.converter.ProcessTypeConverter
import com.luczka.mycoffee.data.local.converter.RoastTypeConverter
import com.luczka.mycoffee.data.local.entity.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.local.entity.BrewEntity
import com.luczka.mycoffee.data.local.entity.CoffeeEntity
import com.luczka.mycoffee.data.local.entity.CoffeeImageEntity
import com.luczka.mycoffee.data.local.entity.EquipmentEntity
import com.luczka.mycoffee.data.local.entity.ReceiptEntity
import com.luczka.mycoffee.data.local.entity.TastingNote
import com.luczka.mycoffee.data.local.entity.TastingNoteCategory

@Database(
    entities = [
        BrewCoffeeCrossRef::class,
        BrewEntity::class,
        CoffeeEntity::class,
        CoffeeImageEntity::class,
        EquipmentEntity.CoffeeMachineEntity::class,
        EquipmentEntity.GrinderEntity::class,
        ReceiptEntity::class,
        TastingNoteCategory::class,
        TastingNote::class
    ],
    version = 37,
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