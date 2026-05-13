package com.android.newsapp.core.local

import android.content.Context
import androidx.room.Room
import com.android.newsapp.favouritenews.data.local.FavouriteNewsDatabase
import com.android.newsapp.favouritenews.data.local.dao.FavouriteNewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): FavouriteNewsDatabase {
        return Room.databaseBuilder(
            context,
            FavouriteNewsDatabase::class.java,
            "favourite_news_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavouriteNewsDb(favouriteNewsDatabase: FavouriteNewsDatabase): FavouriteNewsDao {
        return favouriteNewsDatabase.favouriteDao()
    }
}