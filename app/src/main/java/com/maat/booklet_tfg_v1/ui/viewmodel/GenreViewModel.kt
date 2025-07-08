package com.maat.booklet_tfg_v1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.booklet_tfg_v1.BookletApplication
import com.maat.booklet_tfg_v1.data.entities.Genre
import com.maat.booklet_tfg_v1.data.repository.GenreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GenreViewModel: ViewModel() {
    private val repository: GenreRepository = BookletApplication.genreRepository

    val genres: StateFlow<List<Genre>> = repository.genres
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun getGenreIdByGenreName(genreName: String): Int{
        return repository.getGenreIdByGenreName(genreName)
    }

}