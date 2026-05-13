package com.android.newsapp.favouritenews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.newsapp.favouritenews.data.local.dao.FavouriteNewsDao
import com.android.newsapp.favouritenews.data.local.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1)
abstract class FavouriteNewsDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteNewsDao
}