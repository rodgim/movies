package com.rodgim.movies.framework.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "movie_list",
    primaryKeys = ["category_id", "movie_id"],
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class MovieList(
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    @ColumnInfo(name = "movie_id")
    val movieId: Int
)
