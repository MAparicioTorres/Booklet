package com.maat.booklet_tfg_v1.data.repository

import com.maat.booklet_tfg_v1.data.database.BookDao
import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getAllDetailedBooksFlow(): Flow<List<BookWithAuthorAndGenre>>{
        return bookDao.getAllBooksWithDetailsFlow()
    }


    fun getBookWithAuthorAndGenreByIdFlow(bookId: Int): Flow<BookWithAuthorAndGenre?> {
        return bookDao.getBookWithAuthorAndGenreByIdFlow(bookId)
    }

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun deleteAuthorsWithNoBooks(){
        bookDao.deleteAuthorsWithNoBooks()
    }


    suspend fun deleteBookById(bookId: Int) {
        bookDao.deleteBookById(bookId)
    }

}