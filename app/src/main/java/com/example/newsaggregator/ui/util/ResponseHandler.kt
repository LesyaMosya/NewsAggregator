package com.example.newsaggregator.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newsaggregator.R
import com.example.newsaggregator.data.rss.RssState

@Composable
fun Loading() {
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
        val angle = infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing)
            ), label = "angle"
        )
        Image (
            painter = painterResource(R.drawable.loading),
            contentDescription = "Loading...",
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = angle.value
                }
                .size(60.dp)
        )
    }
}

@Composable
fun Failure(
    failure: RssState.Failure
) {
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(failure.message)
            Text(failure.cause)
        }
    }
}