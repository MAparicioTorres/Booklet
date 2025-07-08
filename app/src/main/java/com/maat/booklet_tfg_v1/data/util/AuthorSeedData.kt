package com.maat.booklet_tfg_v1.data.util

import com.maat.booklet_tfg_v1.data.entities.Author

object AuthorSeedData {
    //Creamos la lista de autores con los que precargaremos la BD.
    val predefinedAuthors = listOf(
        Author(authorName = "Stephen King"),
        Author(authorName = "Andy Weir"),
        Author(authorName = "Jesús Cañadas"),
        Author(authorName = "Susanna Clarke"),
        Author(authorName = "S.A. Cosby"),
        Author(authorName = "Brandon Sanderson"),
        Author(authorName = "Albert Camus"),
        Author(authorName = "Madeline Miller")

    )
}