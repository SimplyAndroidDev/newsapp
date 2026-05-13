package com.android.newsapp.core.network

import com.android.newsapp.newslist.data.repository.NewsListRepositoryImpl
import com.android.newsapp.newslist.domain.NewsListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindNewsRepository(
        newsListRepositoryImpl: NewsListRepositoryImpl
    ): NewsListRepository
}