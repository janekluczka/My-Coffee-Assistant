package com.luczka.mycoffee.data.database.converters

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.database.types.GrinderType

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