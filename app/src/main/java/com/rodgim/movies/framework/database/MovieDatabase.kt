package com.rodgim.movies.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodgim.movies.framework.database.utils.TypeConverter

@Database(entities = [Movie::class, MovieList::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie-db"
        ).build()
    }
    abstract fun movieDao(): MovieDao
}