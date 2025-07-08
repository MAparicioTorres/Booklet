package com.maat.booklet_tfg_v1

import android.app.Application
import com.maat.booklet_tfg_v1.data.database.BookDatabase
import com.maat.booklet_tfg_v1.data.repository.AuthorRepository
import com.maat.booklet_tfg_v1.data.repository.BookRepository
import com.maat.booklet_tfg_v1.data.repository.GenreRepository

class BookletApplication: Application() {
    //Inicializamos la BD, los DAO y los repositorios en esta clase para tener una única instancia y acceder a ellas mediante los modelos de vista.
    override fun onCreate() {
        super.onCreate()
        val database = BookDatabase.getDatabase(this)
        val bookDao = database.bookDao()
        val genreDao = database.genreDao()
        val authorDao = database.authorDao()
        bookRepository = BookRepository(bookDao)
        genreRepository = GenreRepository(genreDao)
        authorRepository = AuthorRepository(authorDao)

    }

    companion object{
        lateinit var bookRepository: BookRepository
        lateinit var genreRepository: GenreRepository
        lateinit var authorRepository: AuthorRepository
    }
}