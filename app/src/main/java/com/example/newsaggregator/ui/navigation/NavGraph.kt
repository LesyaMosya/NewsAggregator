package com.example.newsaggregator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsaggregator.ui.screen.*

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Routes.NewsList.route)
    {
        composable(route = Routes.NewsList.route){
            NewsListScreen(navController)
        }
        composable(route = Routes.NewsItem.route){
            NewsItemScreen(navController)
        }
    }
}