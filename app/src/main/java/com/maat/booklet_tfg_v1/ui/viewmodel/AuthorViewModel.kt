package com.maat.booklet_tfg_v1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.booklet_tfg_v1.BookletApplication
import com.maat.booklet_tfg_v1.data.dto.NormalizedAuthorName
import com.maat.booklet_tfg_v1.data.entities.Author
import com.maat.booklet_tfg_v1.data.repository.AuthorRepository
import kotlinx.coroutines.launch

class AuthorViewModel : ViewModel() {
    private val repository: AuthorRepository = BookletApplication.authorRepository

    suspend fun getNormalizedAuthorsNameList(): List<NormalizedAuthorName> {
        return repository.getNormalizedAuthorName()
    }

    fun updateAuthor(author: Author) {
        viewModelScope.launch {
            repository.updateAuthor(author)
        }
    }

    suspend fun insertAuthorAndGetId(authorName: String): Int {
            return repository.insertAuthorAndGetId(authorName)
    }

}