package com.android.newsapp.newslist.presentation.ui

import com.android.newsapp.newslist.domain.model.News

data class NewsListScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val results: List<News> = emptyList(),
    val isPaginationLoader: Boolean = false
)


data class InternalState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isPaginationLoader: Boolean = false
)