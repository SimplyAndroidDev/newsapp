package com.android.newsapp.favouritenews.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newsapp.favouritenews.domain.usecase.GetFavouriteNewsUseCase
import com.android.newsapp.favouritenews.domain.usecase.RemoveFavouriteNewsUseCase
import com.android.newsapp.newslist.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteNewsViewModel @Inject constructor(
    getFavouriteNewsUseCase: GetFavouriteNewsUseCase,
    private val removeFavouriteNewsUseCase: RemoveFavouriteNewsUseCase
) : ViewModel() {

    val favouriteNewsFlow = getFavouriteNewsUseCase().catch {
        emit(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun removeFavorite(news: News) {
        viewModelScope.launch {
            try {
                removeFavouriteNewsUseCase(news)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}