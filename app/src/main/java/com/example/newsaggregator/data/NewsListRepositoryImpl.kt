package com.example.newsaggregator.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.newsaggregator.data.rss.RssFeed
import com.example.newsaggregator.data.rss.RssState
import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.domain.NewsListRepository
import javax.inject.Inject

class NewsListRepositoryImpl @Inject constructor(
    private val feed: RssFeed
): NewsListRepository {
    val channelInfo: MutableState<RssState> = mutableStateOf(RssState.Empty)

    override suspend fun getNewsList() {
        try {
            channelInfo.apply {
                value = RssState.Loading
                val channel = feed.getRss().channel
                value = RssState.SuccessLoadingNewsList(channel)
            }
        } catch (e: RuntimeException) {
            channelInfo.value = RssState.Failure(e.message.toString(), e.cause.toString())
        }
    }

    override fun getNewsItem(url: String): ItemDto {
        TODO("Not yet implemented")
    }
}