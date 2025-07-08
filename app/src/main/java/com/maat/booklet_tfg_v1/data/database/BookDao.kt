package com.maat.booklet_tfg_v1.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import com.maat.booklet_tfg_v1.data.entities.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM book")
    fun getAllBooksFlow(): Flow<List<Book>>

    //Obtenemos un flujo de un libro con el género y autor.
    @Query("SELECT * FROM book WHERE book_id = :bookId")
    fun getBookWithAuthorAndGenreByIdFlow(bookId: Int): Flow<BookWithAuthorAndGenre?>

    @Query("DELETE FROM book WHERE book_id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    //Eliminamos los autores que no tengan referencias en la tabla libro para limpiar la BD y mejorar rendimiento.
    @Query("DELETE FROM author WHERE author_id NOT IN (SELECT DISTINCT author_id FROM book)")
    suspend fun deleteAuthorsWithNoBooks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBooks(books: List<Book>)

    //Obtenemos un flujo de una lista de libros con su género y autor.
    @Transaction
    @Query("SELECT * FROM book")
    fun getAllBooksWithDetailsFlow(): Flow<List<BookWithAuthorAndGenre>>
}