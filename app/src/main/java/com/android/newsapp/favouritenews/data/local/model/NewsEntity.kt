package com.android.newsapp.favouritenews.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class NewsEntity(
    @PrimaryKey val title: String,
    val author: String?,
    val description: String?,
    val image: String?,
    val source: String?,
    val publishedAt: String?,
    val url: String?
)
