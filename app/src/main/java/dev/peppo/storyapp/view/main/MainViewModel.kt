package dev.peppo.storyapp.view.main

import androidx.lifecycle.ViewModel
import dev.peppo.storyapp.data.StoryRepository
import dev.peppo.storyapp.utils.AppPreferences

class MainViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getStories() = storyRepository.getStories()

}