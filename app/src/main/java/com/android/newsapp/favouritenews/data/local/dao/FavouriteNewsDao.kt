package com.android.newsapp.favouritenews.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.android.newsapp.favouritenews.data.local.model.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteNewsDao {

    @Query("SELECT * from favourites")
    fun getFavouriteNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addFavouriteNews(newsEntity: NewsEntity)

    @Delete
    suspend fun removeFavouriteNews(newsEntity: NewsEntity)
}