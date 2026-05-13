package com.android.newsapp.newslist.data.remote.model

import com.android.newsapp.newslist.domain.model.News

fun NewsItem.toDomain(): News {
    return News(
        author,
        title,
        description,
        image,
        source,
        publishedAt,
        url,
        false
    )
}