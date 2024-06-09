package dev.peppo.storyapp.view.login

import androidx.lifecycle.ViewModel
import dev.peppo.storyapp.data.StoryRepository

class LoginViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email, password)

}