package com.android.newsapp.newslist.data.repository

import com.android.newsapp.core.common.NetworkResult
import com.android.newsapp.core.network.NetworkModule
import com.android.newsapp.newslist.data.remote.api.NewsListApi
import com.android.newsapp.newslist.data.remote.model.toDomain
import com.android.newsapp.newslist.domain.NewsListRepository
import com.android.newsapp.newslist.domain.model.News
import javax.inject.Inject

class NewsListRepositoryImpl @Inject constructor(
    private val newsListApi: NewsListApi
) : NewsListRepository {
    override suspend fun fetchNews(offset: Int, query: String): NetworkResult<List<News>> {
        try {
            val result = newsListApi.fetchNews(
                NetworkModule.API_ACCESS_KEY,
                offset,
                query
            )

            val data = result?.data
            if (data.isNullOrEmpty()) {
                return NetworkResult.Error(
                    message = "Invalid response received!"
                )
            }

            val domainMappedList = data.map {
                it.toDomain()
            }

            return NetworkResult.Success(domainMappedList)
        } catch (e: Exception) {
            return NetworkResult.Error(
                throwable = e,
                message = "Something went wrong!"
            )
        }
    }
}