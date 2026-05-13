package com.android.newsapp.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.newsapp.favouritenews.presentation.ui.FavouriteNewsRoute
import com.android.newsapp.favouritenews.presentation.ui.FavouriteNewsScreen
import com.android.newsapp.newslist.presentation.ui.NewsListRoute
import com.android.newsapp.newslist.presentation.ui.NewsListScreen
import com.android.newsapp.webview.presentation.ui.WebViewScreen
import com.android.newsapp.webview.presentation.ui.WebviewRoute

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val backStack by navController.currentBackStackEntryAsState()
                val currentDestination = backStack?.destination

                bottomTabs.forEach {
                    val isSelected = currentDestination?.hierarchy?.any { nav ->
                        nav.hasRoute(it.route::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = it.tabIcon, contentDescription = it.tabName)
                        },
                        label = {
                            Text(
                                it.tabName,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        })
                }
            }
        }) { innerPadding ->
        NavHost(
            navController,
            startDestination = HomeRoute,
            modifier = modifier.padding(innerPadding)
        ) {
            navigation<HomeRoute>(startDestination = NewsListRoute) {
                composable<NewsListRoute>(
                    deepLinks = listOf(
                        navDeepLink<NewsListRoute>(basePath = "news://list")
                    )
                ) {
                    NewsListScreen {
                        navController.navigate(WebviewRoute(it))
                    }
                }

                composable<WebviewRoute>(
                    deepLinks = listOf(
                        navDeepLink<WebviewRoute>(basePath = "news://view")
                    )
                ) { back ->
                    val route = back.toRoute<WebviewRoute>()
                    WebViewScreen(route.url)
                }
            }

            composable<FavouriteNewsRoute>(
                deepLinks = listOf(
                    navDeepLink<FavouriteNewsRoute>(basePath = "news://favourite")
                )
            ) {
                FavouriteNewsScreen {
                    navController.navigate(WebviewRoute(it))
                }
            }
        }
    }
}