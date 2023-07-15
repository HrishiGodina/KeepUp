package com.example.keepup.network.respository

import com.example.keepup.data.db.AppDatabase
import com.example.keepup.data.model.NewsDataItem
import com.example.keepup.network.client.ApiClient

class NetworkRepository(
    private val db: AppDatabase
) {

    suspend fun getTopHeadlines(countryCode:String, pageNumber:Int)=
        ApiClient.apiInterface.getTopHeadlines(countryCode, pageNumber)

    suspend fun upsert(newsDataItem: NewsDataItem)=
        db.getNewsItemDao().upsert(newsDataItem)

    fun getSavedNewsDataItems()=
        db.getNewsItemDao().getAllNewsDataItems()

    suspend fun deleteNewsDataItem(newsDataItem: NewsDataItem)=
        db.getNewsItemDao().deleteNewsDataItem(newsDataItem)
}