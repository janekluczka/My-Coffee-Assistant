package com.luczka.mycoffee.data.local.converter

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.local.type.CoffeeMachineType

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