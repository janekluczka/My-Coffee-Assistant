package com.luczka.mycoffee.data.local.converter

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.local.type.RoastType

class RoastTypeConverter {

    @TypeConverter
    fun fromRoast(value: RoastType?): Int? {
        return value?.order
    }

    @TypeConverter
    fun toRoast(value: Int?): RoastType? {
        return RoastType.entries.find { it.order == value }
    }
}
