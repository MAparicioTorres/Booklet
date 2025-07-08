package com.maat.booklet_tfg_v1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maat.booklet_tfg_v1.data.converters.ReadingStatusConverter
import com.maat.booklet_tfg_v1.data.entities.Author
import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.entities.Genre
import com.maat.booklet_tfg_v1.data.util.AuthorSeedData
import com.maat.booklet_tfg_v1.data.util.BookSeedData
import com.maat.booklet_tfg_v1.data.util.GenreSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Book::class, Genre::class, Author::class],
    version = 1
)
@TypeConverters(ReadingStatusConverter::class)
abstract class  BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun genreDao(): GenreDao
    abstract fun authorDao(): AuthorDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: BookDatabase? = null


        fun getDatabase(context: Context): BookDatabase {
            //Si ya tenemos una instancia de la BD la devolvemos, sino la creamos.
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object: Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                //Insertamos la lista de géneros predefinidos en la BD.
                                getDatabase(context).genreDao().insertAllGenres(GenreSeedData.predefinedGenres)
                                //Insertamos la lista de autores precargados en la BD.
                                getDatabase(context).authorDao().insertAllAuthors(AuthorSeedData.predefinedAuthors)
                                //Insertamos la lista de libros precargados en la BD.
                                getDatabase(context).bookDao().insertAllBooks(BookSeedData.predefinedBooks)
                            }
                        }
                    })
                    .build()

                DATABASE_INSTANCE = instance
                return instance
            }
        }
    }
}