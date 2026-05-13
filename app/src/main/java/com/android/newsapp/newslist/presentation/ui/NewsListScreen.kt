package com.android.newsapp.newslist.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.newsapp.R
import com.android.newsapp.newslist.domain.model.News
import com.android.newsapp.newslist.presentation.viewmodel.NewsListViewModel
import com.android.newsapp.uicomponents.AnimatedFavIcon

@Composable
fun NewsListScreen(
    modifier: Modifier = Modifier,
    viewmodel: NewsListViewModel = hiltViewModel(),
    openWebView: (url: String) -> Unit
) {
    val screenState by viewmodel.screenState.collectAsStateWithLifecycle()
    val query by viewmodel.searchQuery.collectAsStateWithLifecycle()
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(query = query) {
            viewmodel.onSearchQueryChanged(it)
        }

        if (screenState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
        }

        val results = screenState.results
        if (results.isNotEmpty()) {
            LazyNewsList(
                modifier = Modifier.weight(1f),
                results = results,
                isPaginationLoader = screenState.isPaginationLoader,
                onLoadMore = {
                    viewmodel.fetchNews()
                },
                openWebView = openWebView
            ) {
                viewmodel.toggleFavoriteNews(it)
            }
        }

        if (screenState.isError) {
            Text(
                "Oops, No news found!\nPlease try again later.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = {
                viewmodel.resetAndFetchNews()
            }) {
                Text("Retry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (query: String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
        },
        singleLine = true,
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        placeholder = {
            Text("Search news here!", color = Color.Gray, fontSize = 12.sp)
        },
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun LazyNewsList(
    modifier: Modifier = Modifier,
    results: List<News>,
    isPaginationLoader: Boolean,
    onLoadMore: () -> Unit,
    openWebView: (url: String) -> Unit,
    toggleFavState: (news: News) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            // Load more when reaching near the end of the list
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            val lastItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastItemIndex > totalItems - 5

        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !isPaginationLoader) {
            onLoadMore()
        }
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(results) {
            NewsItemUI(news = it, toggleFavState = toggleFavState, openWebView = openWebView)
        }

        if (isPaginationLoader) {
            item {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun NewsItemUI(
    modifier: Modifier = Modifier,
    news: News,
    toggleFavState: (news: News) -> Unit,
    openWebView: (url: String) -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !news.url.isNullOrEmpty()) {
                news.url?.let { openWebView(it) }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(news.image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.no_image_placeholder).build(),
            "news_image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            news.title?.let { Text(it, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
            news.description?.let {
                Text(
                    it,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            news.author?.let { Text("Author - $it", fontSize = 11.sp) }
            news.publishedAt?.let { Text("Date - $it", fontSize = 11.sp) }

            AnimatedFavIcon(isFav = news.isFavourite, toggleFavState = { toggleFavState(news) })
        }
    }
}