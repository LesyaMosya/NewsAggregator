package com.example.newsaggregator.data.rss

import androidx.lifecycle.SavedStateHandle
import com.example.newsaggregator.data.rss.dto.ItemDto
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
    ): ItemDto {
        val newsUrl = savedStateHandle["newsUrl"] as Any?
        val title = savedStateHandle["title"] as Any?

        return if (newsUrl is String && title is String) ItemDto(title=title, guid=newsUrl)
        else ItemDto()
    }
}

