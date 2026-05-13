package com.android.newsapp.favouritenews.domain.usecase

import com.android.newsapp.favouritenews.data.repository.FavouriteRepository
import com.android.newsapp.newslist.domain.model.News
import javax.inject.Inject

class RemoveFavouriteNewsUseCase @Inject constructor(private val favouriteRepository: FavouriteRepository) {
    suspend operator fun invoke(news: News) {
        return favouriteRepository.removeFavouriteNews(news)
    }
}