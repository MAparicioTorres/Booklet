package com.maat.booklet_tfg_v1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "genre",
    indices = [Index(value = ["genre_name"], unique = true)]
)
data class Genre(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "genre_id") val idGenre: Int = 0,
    @ColumnInfo(name = "genre_name") val genreName: String
)
