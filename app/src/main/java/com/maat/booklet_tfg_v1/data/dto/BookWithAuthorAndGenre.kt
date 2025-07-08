package com.maat.booklet_tfg_v1.data.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.maat.booklet_tfg_v1.data.entities.Author
import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.entities.Genre

//Creamos un DTO que combine las distintas entidades en una única tabla para obtener un objeto con los campos de cada una.
data class BookWithAuthorAndGenre(
    @Embedded val book: Book,
    @Relation(parentColumn = "author_id", entityColumn = "author_id") val author: Author,
    @Relation(parentColumn = "genre_id", entityColumn = "genre_id") val genre: Genre
)