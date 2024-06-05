package dev.peppo.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import dev.peppo.storyapp.data.remote.network.ApiService
import dev.peppo.storyapp.data.remote.response.error.ErrorResponse
import dev.peppo.storyapp.data.remote.response.login.LoginResponse
import dev.peppo.storyapp.data.remote.response.register.RegisterResponse
import dev.peppo.storyapp.data.remote.response.story.StoryResponse
import dev.peppo.storyapp.utils.AppPreferences
import retrofit2.HttpException
import kotlin.Exception

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val appPreferences: AppPreferences
) {

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveToken(token: String) {
        appPreferences.saveLoginSession(token)
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            saveToken(response.loginResult.token)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            appPreferences: AppPreferences
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, appPreferences)
            }.also { instance = it }
    }

}