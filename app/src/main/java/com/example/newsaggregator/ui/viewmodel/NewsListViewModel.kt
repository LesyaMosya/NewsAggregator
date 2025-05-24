package com.example.newsaggregator.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.data.NewsListRepositoryImpl
import com.example.newsaggregator.data.rss.RssState
import com.example.newsaggregator.data.rss.dto.ChannelDto
import com.example.newsaggregator.domain.usecase.GetNewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    repository: NewsListRepositoryImpl
) : ViewModel() {

    private val getNewsListUseCase = GetNewsListUseCase(repository)

    private var _channel: MutableState<RssState> = repository.channelInfo
    val channel = _channel

    private val _isFilterSelected = mutableStateOf(false)
    val isFilterSelected = _isFilterSelected

    init {
        fetchData()
    }

    fun createEvent(e: Event) {
        onEvent(e)
    }

    private fun onEvent(e: Event) {
        when (e) {
            is Event.SortedListByDatePub -> {
                _isFilterSelected.value = !_isFilterSelected.value
                sortedNewsListByDatePub()
            }

            Event.UpdateContent -> {
                fetchData()
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getNewsListUseCase.getNewsList()
            sortedNewsListByDatePub()
        }
    }

    private fun sortedNewsListByDatePub() {
        if (_channel.value is RssState.SuccessLoadingNewsList) {
            val sortedList = if (isFilterSelected.value) {
                (_channel.value as RssState.SuccessLoadingNewsList).data.items.sortedBy { it.dcDate }

            } else {
                (_channel.value as RssState.SuccessLoadingNewsList).data.items.sortedByDescending { it.dcDate }
            }

            _channel.value = RssState.SuccessLoadingNewsList(
                ChannelDto(
                    title = (_channel.value as RssState.SuccessLoadingNewsList).data.title,
                    items = sortedList
                )
            )
        }
    }

}