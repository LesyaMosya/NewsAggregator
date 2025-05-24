package com.example.newsaggregator.ui.viewmodel

sealed class Event {
    data object SortedListByDatePub : Event()
    data object UpdateContent : Event()
}