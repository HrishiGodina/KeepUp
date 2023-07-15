package com.example.keepup.data.model

data class AllNewsResponse(
    var articles: MutableList<NewsDataItem>,
    val status: String,
    val totalResults: Int
)