package com.android.newsapp.newslist.domain

import com.android.newsapp.core.common.NetworkResult
import com.android.newsapp.newslist.domain.model.News

interface NewsListRepository {
    suspend fun fetchNews(offset: Int, query: String = ""): NetworkResult<List<News>>
}