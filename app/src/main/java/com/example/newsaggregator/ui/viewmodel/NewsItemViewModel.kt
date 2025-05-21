package com.example.newsaggregator.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsaggregator.data.NewsListRepositoryImpl
import com.example.newsaggregator.domain.usecase.GetNewsItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsItemViewModel @Inject constructor(
    private val response: List<String>,
) : ViewModel() {

    val title: String = response[0]
    val news: String = response[1]
}