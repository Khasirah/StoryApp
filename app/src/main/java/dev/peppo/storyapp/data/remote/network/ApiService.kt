package dev.peppo.storyapp.data.remote.network

import dev.peppo.storyapp.data.remote.response.login.LoginResponse
import dev.peppo.storyapp.data.remote.response.register.RegisterResponse
import dev.peppo.storyapp.data.remote.response.story.ListStoryItem
import dev.peppo.storyapp.data.remote.response.story.StoryResponse
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("stories")
    suspend fun getStories() : StoryResponse

}