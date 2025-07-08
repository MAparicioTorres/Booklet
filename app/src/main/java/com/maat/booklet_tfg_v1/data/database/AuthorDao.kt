package com.maat.booklet_tfg_v1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maat.booklet_tfg_v1.data.dto.NormalizedAuthorName
import com.maat.booklet_tfg_v1.data.entities.Author

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAuthors(authors: List<Author>)

    //Obtenemos el nombre del autor en minúsculas.
    @Query("SELECT author_id, LOWER(author_name) AS normalizedAuthorName FROM author")
    suspend fun getNormalizedAuthorName(): List<NormalizedAuthorName>

    @Update
    suspend fun updateAuthor(author: Author)

    @Insert
    suspend fun insertAuthorAndGetId(author: Author): Long
}