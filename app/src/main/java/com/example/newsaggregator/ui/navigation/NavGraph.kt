package com.example.newsaggregator.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsaggregator.ui.screen.*

@RequiresApi(Build.VERSION_CODES.O)
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