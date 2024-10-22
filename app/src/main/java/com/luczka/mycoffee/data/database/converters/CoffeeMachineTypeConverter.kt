package com.luczka.mycoffee.data.database.converters

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.database.types.CoffeeMachineType

class CoffeeMachineTypeConverter {

    @TypeConverter
    fun fromCoffeeMachineType(value: CoffeeMachineType): String {
        return value.name
    }

    @TypeConverter
    fun toCoffeeMachineType(value: String): CoffeeMachineType {
        return CoffeeMachineType.valueOf(value)
    }

}