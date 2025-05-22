package com.example.newsaggregator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.data.NewsListRepositoryImpl
import com.example.newsaggregator.domain.usecase.GetNewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsListRepositoryImpl
) : ViewModel() {

    private val getNewsListUseCase = GetNewsListUseCase(repository)
    val channel = repository.channelInfo

    init {
        viewModelScope.launch {
            getNewsListUseCase.getNewsList()
        }
    }

}