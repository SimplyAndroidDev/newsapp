package com.android.newsapp.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector
import com.android.newsapp.favouritenews.presentation.ui.FavouriteNewsRoute
import com.android.newsapp.newslist.presentation.ui.NewsListRoute
import kotlinx.serialization.Serializable

sealed class HomeTabs(val tabName: String, val route: @Serializable Any, val tabIcon: ImageVector) {
    object NewsList : HomeTabs("News", NewsListRoute, Icons.Default.Newspaper)
    object Favourites : HomeTabs("Favourites", FavouriteNewsRoute, Icons.Default.Favorite)
}

val bottomTabs = listOf(HomeTabs.NewsList, HomeTabs.Favourites)