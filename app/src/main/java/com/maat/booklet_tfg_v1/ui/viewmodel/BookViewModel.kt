package com.maat.booklet_tfg_v1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.booklet_tfg_v1.BookletApplication
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.models.ReadingStatus
import com.maat.booklet_tfg_v1.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookViewModel() : ViewModel() {
    private val repository: BookRepository = BookletApplication.bookRepository


    val allDetailedBooksFlow: StateFlow<List<BookWithAuthorAndGenre>> =
        repository.getAllDetailedBooksFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun getBooksByStatus(status: ReadingStatus): StateFlow<List<BookWithAuthorAndGenre>>{
        //Filtramos los libros de la lista en base a su estado de lectura.
        return allDetailedBooksFlow.map { books -> books.filter { it.book.readingStatus == status } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateBook(book)
        }
    }

    fun deleteAuthorsWithNoBooks() {
        viewModelScope.launch {
            repository.deleteAuthorsWithNoBooks()
        }
    }


    fun deleteBookById(bookId: Int) {
        viewModelScope.launch {
            repository.deleteBookById(bookId)
        }
    }

    fun getBookWithAuthorAndGenreByIdFlow(bookId: Int): Flow<BookWithAuthorAndGenre?> {
        return repository.getBookWithAuthorAndGenreByIdFlow(bookId)
    }
}