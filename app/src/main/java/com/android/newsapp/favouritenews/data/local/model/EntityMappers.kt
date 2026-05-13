package com.android.newsapp.favouritenews.data.local.model

import com.android.newsapp.newslist.domain.model.News

fun News.toEntity(): NewsEntity {
    return NewsEntity(
        title ?: "default_news",
        author,
        description,
        image,
        source,
        publishedAt,
        url
    )
}

fun NewsEntity.toDomain(): News {
    return News(
        author,
        title,
        description,
        image,
        source,
        publishedAt,
        url,
        true
    )
}