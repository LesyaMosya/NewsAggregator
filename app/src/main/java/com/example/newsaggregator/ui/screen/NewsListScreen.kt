package com.example.newsaggregator.ui.screen

import android.text.Html
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsaggregator.R
import com.example.newsaggregator.data.rss.RssState
import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.ui.util.*
import com.example.newsaggregator.ui.viewmodel.NewsListViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(navController: NavController) {
    val viewModel: NewsListViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val channel = viewModel.channel.value
                    Text(
                        text = if (channel is RssState.SuccessLoadingNewsList) channel.data.title
                        else "",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    )
    {innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            NewsList(navController, viewModel)
        }
    }
}

@Composable
fun NewsList(
    navController: NavController,
    viewModel: NewsListViewModel
) {
    when (val result = viewModel.channel.value) {
        is RssState.SuccessLoadingNewsList -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(result.data.items) { news ->
                    NewsCard(news, navController)
                }
            }
        }

        is RssState.Loading -> {
            Loading()
        }

        is RssState.Failure -> {
            Failure(result)

        }

        else -> {}
    }
}

@Composable
fun NewsCard(
    news: ItemDto,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .clickable {
                navController.navigate("news_item_screen/${news.title}/${URLEncoder.encode(news.guid, "UTF8")}")
            }
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(7.dp, 0.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ) {

        Column (
            modifier = Modifier.weight(1f).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
//                modifier = Modifier.weight(1f)
            )

            Text(
                text = Html.fromHtml(news.description, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.weight(4f)
            )
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(news.contents[1].url)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.weight(1f)
        )
    }
}