package com.android.newsapp.favouritenews.domain.usecase

import com.android.newsapp.favouritenews.data.repository.FavouriteRepository
import com.android.newsapp.newslist.domain.model.News
import javax.inject.Inject

class ToggleFavouriteNewsUseCase @Inject constructor(private val favouriteRepository: FavouriteRepository) {
    suspend operator fun invoke(news: News) {
        if (news.isFavourite) {
            favouriteRepository.removeFavouriteNews(news)
        } else {
            favouriteRepository.addFavouriteNews(news)
        }
    }
}