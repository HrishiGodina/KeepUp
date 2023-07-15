package com.example.keepup.network.api

import com.example.keepup.BuildConfig.API_KEY
import com.example.keepup.data.model.AllNewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode: String = "in", //default to India

        @Query("page")
        pageNumber: Int= 1,

        @Query("apiKey")
        apiKey: String= API_KEY

    ):Response<AllNewsResponse>
}