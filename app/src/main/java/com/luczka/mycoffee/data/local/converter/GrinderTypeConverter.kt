package com.luczka.mycoffee.data.local.converter

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.local.type.GrinderType

class GrinderTypeConverter {

    @TypeConverter
    fun fromGrinderType(value: GrinderType): String {
        return value.name
    }

    @TypeConverter
    fun toGrinderType(value: String): GrinderType {
        return GrinderType.valueOf(value)
    }
}