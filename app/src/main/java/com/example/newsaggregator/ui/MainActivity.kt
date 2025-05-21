package com.example.newsaggregator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.newsaggregator.ui.navigation.NavGraph
import com.example.newsaggregator.ui.theme.NewsAggregatorTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAggregatorTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}