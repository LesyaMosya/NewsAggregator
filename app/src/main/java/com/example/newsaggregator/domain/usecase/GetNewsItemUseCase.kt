package com.example.newsaggregator.domain.usecase

import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.domain.NewsListRepository

class GetNewsItemUseCase(private val repository: NewsListRepository) {

    fun getNewsItem(url: String): ItemDto {
        return repository.getNewsItem(url)
    }
}