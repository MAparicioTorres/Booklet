package com.maat.booklet_tfg_v1.data.repository

import com.maat.booklet_tfg_v1.data.database.AuthorDao
import com.maat.booklet_tfg_v1.data.dto.NormalizedAuthorName
import com.maat.booklet_tfg_v1.data.entities.Author

class AuthorRepository(private val authorDao: AuthorDao) {

    suspend fun getNormalizedAuthorName(): List<NormalizedAuthorName> {
        return authorDao.getNormalizedAuthorName()
    }

    suspend fun updateAuthor(author: Author) {
        return authorDao.updateAuthor(author)
    }


    suspend fun insertAuthorAndGetId(authorName: String): Int {
        val author = Author(
            authorName = authorName
        )
        //Obtenemos el ID del registro en la tabla autor.
        val newId = authorDao.insertAuthorAndGetId(author)
        return newId.toInt()
    }
}