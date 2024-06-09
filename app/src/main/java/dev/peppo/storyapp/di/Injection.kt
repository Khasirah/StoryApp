package dev.peppo.storyapp.di

import android.content.Context
import dev.peppo.storyapp.data.StoryRepository
import dev.peppo.storyapp.data.remote.network.ApiConfig
import dev.peppo.storyapp.utils.AppPreferences
import dev.peppo.storyapp.utils.dataStore

object Injection {
    fun provideRepository(context: Context) : StoryRepository {
        val appPreferences = AppPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(appPreferences)
        return StoryRepository.getInstance(apiService, appPreferences)
    }
}