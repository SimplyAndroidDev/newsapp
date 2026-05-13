package com.android.newsapp.newslist.domain.usecase

import com.android.newsapp.core.common.NetworkResult
import com.android.newsapp.newslist.domain.NewsListRepository
import com.android.newsapp.newslist.domain.model.News
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(private val repository: NewsListRepository) {
    //makes usecase appear like a function call and reduces object creation boilerplate code,
    //also since it has just single responsibility of fetching news so operator fun  invoke is used here
    suspend operator fun invoke(offset: Int, query: String): NetworkResult<List<News>> {
        return repository.fetchNews(offset, query)
    }
}