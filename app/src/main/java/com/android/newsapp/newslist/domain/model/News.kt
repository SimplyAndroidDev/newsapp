package com.android.newsapp.newslist.domain.model

data class News(
    val author: String?,
    val title: String?,
    val description: String?,
    val image: String?,
    val source: String?,
    val publishedAt: String?,
    val url: String?,
    val isFavourite: Boolean
)