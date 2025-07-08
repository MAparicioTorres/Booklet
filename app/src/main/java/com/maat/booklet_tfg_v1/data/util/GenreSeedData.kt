package com.maat.booklet_tfg_v1.data.util

import com.maat.booklet_tfg_v1.data.entities.Genre

object GenreSeedData {
    //Creamos la lista de géneros predefinidos entre los que podremos escoger.
    val predefinedGenres = listOf(
        Genre(genreName = "Fantasía"),
        Genre(genreName = "Ciencia ficción"),
        Genre(genreName = "Misterio"),
        Genre(genreName = "Suspense"),
        Genre(genreName = "Terror"),
        Genre(genreName = "Romance"),
        Genre(genreName = "Drama"),
        Genre(genreName = "Ficción histórica"),
        Genre(genreName = "Histórico"),
        Genre(genreName = "Novela gráfica"),
        Genre(genreName = "Aventura"),
        Genre(genreName = "Clásico")
    )
}