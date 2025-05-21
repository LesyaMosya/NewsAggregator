package com.example.newsaggregator.data.rss

import com.example.newsaggregator.data.rss.dto.ChannelDto
import com.example.newsaggregator.data.rss.dto.ItemDto

sealed class RssState {
    open class SuccessLoadingNewsList(val data: ChannelDto) : RssState()
    open class SuccessLoadingNewsItem(val data: ItemDto) : RssState()

    class Failure(val message: String, val cause: String) : RssState()

    data object Empty : RssState()
    data object Loading : RssState()
}