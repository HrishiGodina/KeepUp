package com.example.keepup.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.keepup.data.model.NewsDataItem

@Dao

interface NewsItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(newsDataItem: NewsDataItem): Long

    @Query("SELECT* FROM NewsDataItem")
    fun getAllNewsDataItems():LiveData<List<NewsDataItem>>

    @Delete
    suspend fun deleteNewsDataItem(newsDataItem: NewsDataItem)
}