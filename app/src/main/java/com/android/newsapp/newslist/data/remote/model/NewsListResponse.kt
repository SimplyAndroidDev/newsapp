package com.android.newsapp.newslist.data.remote.model

import com.google.gson.annotations.SerializedName

data class NewsListModel(
    @SerializedName("data")
    private val _data: List<NewsItem?>?
) {
    val data: List<NewsItem>?
        get() = _data?.filterNotNull()
}

data class NewsItem(
    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("source")
    val source: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("published_at")
    val publishedAt: String?
)