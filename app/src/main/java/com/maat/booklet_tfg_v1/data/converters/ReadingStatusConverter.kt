package com.maat.booklet_tfg_v1.data.converters

import androidx.room.TypeConverter
import com.maat.booklet_tfg_v1.data.models.ReadingStatus

class ReadingStatusConverter {

    //Realizamos la conversión a un tipo interpretable por la BD.
    @TypeConverter
    fun fromReadingStatus(status: ReadingStatus): String{
        return status.name
    }

    //Hacemos la recomposición de String al tipo ReadingStatus.
    @TypeConverter
    fun toReadingStatus(status: String): ReadingStatus{
        return ReadingStatus.valueOf(status)
    }
}



