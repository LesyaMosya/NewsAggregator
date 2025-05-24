package com.example.newsaggregator.ui.screen

import android.content.Intent
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.newsaggregator.ui.viewmodel.Event
import com.example.newsaggregator.ui.viewmodel.NewsListViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

@RequiresApi(Build.VERSION_CODES.O)
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
                actions = {
                    SortButton(viewModel)
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
fun SortButton(
    viewModel: NewsListViewModel
) {
    val isSelected = viewModel.isFilterSelected

    Surface (
        modifier = Modifier
            .size(45.dp)
            .clickable {
                viewModel.createEvent(Event.SortedListByDatePub)
            },
        shape = RectangleShape,
        color = if (isSelected.value) MaterialTheme.colorScheme.secondaryContainer
        else Color.Transparent
    ) {

        Image(
            painter = if (isSelected.value) {
                painterResource(R.drawable.descending_btn)
            } else {
                painterResource(R.drawable.ascending_btn)
            },
            contentDescription = stringResource(R.string.sorted_str),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.padding(5.dp)
        )
    }

    Spacer(modifier = Modifier.width(15.dp))
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsList(
    navController: NavController,
    viewModel: NewsListViewModel
) {
    var result = viewModel.channel.value

    val coroutineScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() =
        coroutineScope.launch {
            refreshing = true
            viewModel.createEvent(Event.UpdateContent)
            refreshing = false
        }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        when (result) {
            is RssState.SuccessLoadingNewsList -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (!refreshing) {
                        items(result.data.items) { news ->
                            NewsCard(news, navController)
                        }
                    }
                }
            }

            is RssState.Loading -> {
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(70.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer,
                        strokeWidth = 7.dp
                    )
                }
            }

            is RssState.Failure -> {
                Failure(result, viewModel)
            }

            else -> {}
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsCard(
    news: ItemDto,
    navController: NavController
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, news.guid)
        type = "text/x-uri"
    }

    val context = LocalContext.current
    val shareIntent = Intent.createChooser(sendIntent, null)


    Row(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(2.dp, 0.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .height(200.dp)
            .combinedClickable(
                onLongClick = { context.startActivity(shareIntent) },
                onClick = {
                    navController.navigate(
                        "news_item_screen/${news.title}/${
                            URLEncoder.encode(
                                news.guid,
                                "UTF8"
                            )
                        }"
                    )
                }
            )
    ) {

        Column (
            modifier = Modifier
                .weight(1f)
                .padding(5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Text(
                text = Html.fromHtml(
                    news.description,
                    HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                ).toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = if (news.dcCreator != "") news.dcCreator else "Unknown author",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(news.contents[1].url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxHeight()
            )
            Box (
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .alpha(0.8f)
                    .background(MaterialTheme.colorScheme.secondary, RectangleShape)
                    .padding(3.dp, 0.dp)
            ) {
                Text(
                    text = calculateCountOfHours(news.dcDate),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Box (
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .alpha(0.8f)
                    .background(MaterialTheme.colorScheme.secondary, RectangleShape)
                    .padding(3.dp, 0.dp)
            ) {
                Text(
                    text = news.contents[0].credit?.value?.reduction("Photograph: ", "Ph: ")
                        ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

