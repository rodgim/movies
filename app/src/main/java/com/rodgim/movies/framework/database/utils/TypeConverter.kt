package com.rodgim.movies.framework.database.utils

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rodgim.entities.Genre
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@TypeConverters
class TypeConverter : KoinComponent {
    private val gson: Gson by inject()

    @TypeConverter
    fun genreListToStr(list: List<Genre>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun strToGenreList(str: String?): List<Genre>? {
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(str, type)
    }
}