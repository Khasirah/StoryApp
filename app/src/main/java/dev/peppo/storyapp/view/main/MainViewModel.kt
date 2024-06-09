package dev.peppo.storyapp.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dev.peppo.storyapp.data.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getToken() = storyRepository.getToken()

    fun getStories() = storyRepository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

}