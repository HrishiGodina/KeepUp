package com.example.keepup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "NewsDataItem"
)
data class NewsDataItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int?= null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var isRestricted: Boolean
) : Serializable