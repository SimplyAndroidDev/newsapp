package com.android.newsapp.newslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newsapp.core.common.NetworkResult
import com.android.newsapp.favouritenews.domain.usecase.GetFavouriteNewsUseCase
import com.android.newsapp.favouritenews.domain.usecase.ToggleFavouriteNewsUseCase
import com.android.newsapp.newslist.domain.model.News
import com.android.newsapp.newslist.domain.usecase.GetNewsListUseCase
import com.android.newsapp.newslist.presentation.ui.InternalState
import com.android.newsapp.newslist.presentation.ui.NewsListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase,
    favouriteNewsUseCase: GetFavouriteNewsUseCase,
    private val toggleFavouriteNewsUseCase: ToggleFavouriteNewsUseCase
) : ViewModel() {
    private val _rawNewsResults = MutableStateFlow<List<News>>(emptyList())

    private val _internalState = MutableStateFlow(InternalState())

    private var offset = 0
    private var fetchNewsJob: Job? = null

    val screenState = combine(
        _rawNewsResults,
        favouriteNewsUseCase()
            .catch { emit(emptyList()) },
        _internalState
    ) { rawNews, favNews, internalState ->
        val mappedResults = rawNews.map { news ->
            news.copy(isFavourite = favNews.find {
                it.title.equals(news.title, true)
            } != null)
        }

        NewsListScreenState(
            results = mappedResults,
            isLoading = internalState.isLoading,
            isPaginationLoader = internalState.isPaginationLoader,
            isError = internalState.isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewsListScreenState(isLoading = true)
    )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _searchQueryFlow = MutableStateFlow("")
    val searchQuery = _searchQueryFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect {
                    resetAndFetchNews(it)
                }
        }
    }

    fun resetAndFetchNews(query: String = _searchQueryFlow.value) {
        fetchNewsJob?.cancel()
        offset = 0
        _rawNewsResults.value = emptyList()
        _internalState.value = _internalState.value.copy(
            isLoading = true,
            isPaginationLoader = false,
            isError = false
        )

        fetchNews(query)
    }

    fun fetchNews(query: String = _searchQueryFlow.value) {
        if (offset == -1 || _internalState.value.isPaginationLoader) return

        val isFirstLoad = _rawNewsResults.value.isEmpty()
        val currentInternalState = _internalState.value
        _internalState.value = currentInternalState.copy(
            isLoading = isFirstLoad,
            isPaginationLoader = !isFirstLoad,
            isError = false
        )

        fetchNewsJob = viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    getNewsListUseCase(offset, query)
                }

                when (result) {
                    is NetworkResult.Success -> {
                        _rawNewsResults.value += result.result
                        _internalState.value = currentInternalState.copy(
                            isLoading = false, isError = false, isPaginationLoader = false
                        )
                        offset += 25
                    }

                    is NetworkResult.Error -> {
                        _internalState.value = currentInternalState.copy(
                            isLoading = false,
                            isPaginationLoader = false,
                            isError = _rawNewsResults.value.isEmpty()
                        )
                        offset = -1
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _internalState.value = currentInternalState.copy(
                    isLoading = false,
                    isPaginationLoader = false,
                    isError = _rawNewsResults.value.isEmpty()
                )
                offset = -1
            }
        }
    }

    fun toggleFavoriteNews(news: News) {
        viewModelScope.launch {
            try {
                toggleFavouriteNewsUseCase(news)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQueryFlow.value = newQuery
    }
}