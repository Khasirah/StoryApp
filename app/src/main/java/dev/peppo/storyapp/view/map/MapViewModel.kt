package dev.peppo.storyapp.view.map

import androidx.lifecycle.ViewModel
import dev.peppo.storyapp.data.StoryRepository

class MapViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {
    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}