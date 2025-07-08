package com.maat.booklet_tfg_v1.data.dto

import androidx.room.ColumnInfo

//Creamos un DTO que nos permita extraer el nombre tratado del autor en el campo «normalizedAuthorName».
data class NormalizedAuthorName(
    @ColumnInfo(name="author_id") val authorId: Int,
    val normalizedAuthorName: String
)
