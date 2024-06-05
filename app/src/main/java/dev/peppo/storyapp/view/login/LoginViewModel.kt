package dev.peppo.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.peppo.storyapp.data.Result
import dev.peppo.storyapp.data.StoryRepository
import dev.peppo.storyapp.data.remote.response.login.LoginResponse
import dev.peppo.storyapp.utils.AppPreferences
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun login(email: String, password: String) = storyRepository.login(email, password)

}