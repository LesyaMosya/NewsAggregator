package com.example.newsaggregator.data.rss

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NewsItemNavigationArgModule {

    @Provides
    @ViewModelScoped
    fun provideNewsUrl(
        savedStateHandle: SavedStateHandle
    ): List<String> {
        val newsUrl = checkNotNull(savedStateHandle["newsUrl"])
        val title = checkNotNull(savedStateHandle["title"])
        return if (newsUrl is String && title is String) listOf(title, newsUrl)
        else emptyList()
    }
}

