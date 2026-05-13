package com.android.newsapp.favouritenews.data.repository

import com.android.newsapp.favouritenews.data.local.dao.FavouriteNewsDao
import com.android.newsapp.favouritenews.data.local.model.toDomain
import com.android.newsapp.favouritenews.data.local.model.toEntity
import com.android.newsapp.newslist.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepository @Inject constructor(private val favouriteNewsDao: FavouriteNewsDao) {
    fun getFavouriteNewsList(): Flow<List<News>> {
        return favouriteNewsDao.getFavouriteNews().map {
            it.map { newsEntity ->
                newsEntity.toDomain()
            }
        }
    }

    suspend fun addFavouriteNews(news: News) =
        favouriteNewsDao.addFavouriteNews(news.toEntity())

    suspend fun removeFavouriteNews(news: News) =
        favouriteNewsDao.removeFavouriteNews(news.toEntity())
}