package com.android.newsapp.favouritenews.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.newsapp.favouritenews.presentation.viewmodel.FavouriteNewsViewModel
import com.android.newsapp.newslist.domain.model.News
import com.android.newsapp.newslist.presentation.ui.NewsItemUI

@Composable
fun FavouriteNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouriteNewsViewModel = hiltViewModel(),
    openWebView: (url: String) -> Unit
) {
    val result by viewModel.favouriteNewsFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (result.isEmpty()) {
            Text("No FavouriteNews yet!", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        } else {
            FavouriteNewsList(results = result, openWebView = openWebView) { news ->
                viewModel.removeFavorite(news)
            }
        }
    }
}

@Composable
fun FavouriteNewsList(
    modifier: Modifier = Modifier,
    results: List<News>,
    openWebView: (url: String) -> Unit,
    toggleFavState: (news: News) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(results) {
            NewsItemUI(
                news = it,
                toggleFavState = { toggleFavState(it) },
                openWebView = openWebView
            )
        }
    }
}