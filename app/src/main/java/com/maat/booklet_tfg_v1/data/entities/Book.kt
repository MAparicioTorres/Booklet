package com.maat.booklet_tfg_v1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maat.booklet_tfg_v1.data.models.ReadingStatus

@Entity(
    tableName = "book",
    foreignKeys = [
        ForeignKey(
            entity = Genre::class,
            parentColumns = ["genre_id"],
            childColumns = ["genre_id"],
            //No queremos que se elimine la entrada en la BD si el registro del libro apunta a dicho género.
            onDelete = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = Author::class,
            parentColumns = ["author_id"],
            childColumns = ["author_id"],
            //No queremos que se elimine la entrada en la BD si el registro del libro apunta a dicho autor.
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("genre_id"), Index("author_id")])

data class Book(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "book_id") val bookId: Int = 0,
    @ColumnInfo(name = "book_cover_url") val bookCoverUrl: String = "",
    val title: String,
    @ColumnInfo(name="author_id") val authorId: Int,
    @ColumnInfo(name = "genre_id") val genreId: Int,
    //Establecemos como estado de lectura predeterminado «Pendiente».
    @ColumnInfo(name = "reading_status") val readingStatus: ReadingStatus = ReadingStatus.TO_READ,
    @ColumnInfo(name = "start_date") val startDate: String? = null,
    @ColumnInfo(name = "end_date") val endDate: String? = null,
    val summary: String? = null,
    //Float porque solo necesitamos valores del 0 al 10 con dos decimales máximo.
    val rating: Float? = null
)
