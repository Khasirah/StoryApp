package dev.peppo.storyapp.view.register

import androidx.lifecycle.ViewModel
import dev.peppo.storyapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}