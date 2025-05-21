package com.example.newsaggregator.domain.usecase

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import com.example.newsaggregator.data.rss.RssState
import com.example.newsaggregator.data.rss.dto.ChannelDto
import com.example.newsaggregator.domain.NewsListRepository

class GetNewsListUseCase (private val repository: NewsListRepository) {

    suspend fun getNewsList() {
        repository.getNewsList()
    }
}