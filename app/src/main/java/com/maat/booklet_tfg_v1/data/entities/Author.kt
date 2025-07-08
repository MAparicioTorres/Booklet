package com.maat.booklet_tfg_v1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "author",
    indices = [Index(value = ["author_name"], unique = true)])
data class Author(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "author_id") val authorId: Int = 0,
    @ColumnInfo(name = "author_name") val authorName: String
)
