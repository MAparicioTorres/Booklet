package com.maat.booklet_tfg_v1.data.repository

import com.maat.booklet_tfg_v1.data.database.GenreDao
import com.maat.booklet_tfg_v1.data.entities.Genre
import kotlinx.coroutines.flow.Flow

class GenreRepository (private val genreDao: GenreDao) {

    //Creamos una variable que albergue un flujo de la lista con los géneros predefinidos.
    val genres: Flow<List<Genre>> = genreDao.getAllGenres()

    suspend fun getGenreIdByGenreName(genreName: String): Int{
        return genreDao.getGenreIdByGenreName(genreName)
    }

}