package com.maat.booklet_tfg_v1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maat.booklet_tfg_v1.data.entities.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    //Obtenemos un flujo de una lista con los géneros ordenados alfabéticamente por su nombre.
    @Query("SELECT * FROM genre ORDER BY genre_name")
    fun getAllGenres(): Flow<List<Genre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGenres(genres: List<Genre>)

    //Obtenemos el ID de un género comparando ambos nombres convirtiéndolos a minúscula.
    @Query("SELECT genre_id FROM genre WHERE LOWER(genre_name) = LOWER(:genreName)")
    suspend fun getGenreIdByGenreName(genreName: String): Int
}