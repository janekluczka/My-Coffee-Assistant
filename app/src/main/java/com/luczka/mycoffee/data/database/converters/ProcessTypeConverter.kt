package com.luczka.mycoffee.data.database.converters

import androidx.room.TypeConverter
import com.luczka.mycoffee.data.database.types.ProcessType

class ProcessTypeConverter {

    @TypeConverter
    fun fromProcess(value: ProcessType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toProcess(value: String?): ProcessType? {
        return value?.let { ProcessType.valueOf(it) }
    }

}