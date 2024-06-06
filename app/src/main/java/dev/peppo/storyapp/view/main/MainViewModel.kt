package dev.peppo.storyapp.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.peppo.storyapp.data.StoryRepository
import dev.peppo.storyapp.utils.AppPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getStories() = storyRepository.getStories()

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

}