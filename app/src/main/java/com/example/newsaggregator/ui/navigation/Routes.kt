package com.example.newsaggregator.ui.navigation

sealed class Routes(val route: String) {
    object NewsList: Routes("news_list_screen")
    object NewsItem: Routes("news_item_screen/{title}/{newsUrl}")
}