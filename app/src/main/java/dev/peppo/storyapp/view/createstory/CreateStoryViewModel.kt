package dev.peppo.storyapp.view.createstory

import androidx.lifecycle.ViewModel
import dev.peppo.storyapp.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {
    fun createStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody
    ) = storyRepository.createStory(multipartBody, description)
}