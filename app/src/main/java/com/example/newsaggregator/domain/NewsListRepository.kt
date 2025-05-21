package com.example.newsaggregator.domain

import com.example.newsaggregator.data.rss.dto.ItemDto

interface NewsListRepository {

    suspend fun getNewsList()

    fun getNewsItem(url: String): ItemDto
}