package com.android.newsapp.newslist.data.remote.api

import com.android.newsapp.newslist.data.remote.model.NewsListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsListApi {
    @GET("news")
    suspend fun fetchNews(
        @Query("access_key") accessKey: String,
        @Query("offset") offset: Int,
        @Query("keywords") query: String
    ): NewsListModel?
}