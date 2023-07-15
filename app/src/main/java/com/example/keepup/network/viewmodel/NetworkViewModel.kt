package com.example.keepup.network.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.keepup.data.model.AllNewsResponse
import com.example.keepup.data.model.NewsDataItem
import com.example.keepup.data.model.Source
import com.example.keepup.network.respository.NetworkRepository
import com.example.keepup.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NetworkViewModel(
    app: Application,
    private val networkRepository: NetworkRepository
) : AndroidViewModel(app) {

    val topHeadLines: MutableLiveData<Resource<AllNewsResponse>> = MutableLiveData()

    //Pagination
    var topHeadLinesNewsPage = 1
    var topHeadLinesNewsResponse: AllNewsResponse? = null


    init {
        getTopHeadlines("in")
    }

    fun getTopHeadlines(countryCode: String) = viewModelScope.launch {
        getTopHeadlinesNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<AllNewsResponse>): Resource<AllNewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                topHeadLinesNewsPage++
                if (topHeadLinesNewsResponse == null) {
                    topHeadLinesNewsResponse = resultResponse
                } else {
                    val oldArticles = topHeadLinesNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }

                val newList = topHeadLinesNewsResponse?.articles

                val source = Source("", "TDB")
                val newsDataItem = NewsDataItem(0, "", "", "", "", source, "", "", "", true)
                newList?.add(3, newsDataItem)
                topHeadLinesNewsResponse?.articles = newList!!
                return Resource.Success(topHeadLinesNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveNewsItem(newsDataItem: NewsDataItem) = viewModelScope.launch {
        networkRepository.upsert(newsDataItem)
    }

    fun getSavedNewsDataItems() = networkRepository.getSavedNewsDataItems()

    fun deleteSavedNewsDataItem(newsDataItem: NewsDataItem) = viewModelScope.launch {
        networkRepository.deleteNewsDataItem(newsDataItem)
    }

    private suspend fun getTopHeadlinesNewsCall(countryCode: String) {
        topHeadLines.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = networkRepository.getTopHeadlines(countryCode, topHeadLinesNewsPage)
                topHeadLines.postValue(handleBreakingNewsResponse(response))
            } else {
                topHeadLines.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> topHeadLines.postValue(Resource.Error("Network Failure"))
                else -> topHeadLines.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}