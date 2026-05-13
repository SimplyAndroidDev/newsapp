package com.android.newsapp.favouritenews.domain.usecase

import com.android.newsapp.favouritenews.data.repository.FavouriteRepository
import com.android.newsapp.newslist.domain.model.News
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteNewsUseCase @Inject constructor(private val favouriteRepository: FavouriteRepository) {
    operator fun invoke(): Flow<List<News>> {
        return favouriteRepository.getFavouriteNewsList()
    }
}