package com.andi.storyapp.network

import com.andi.storyapp.model.response.*
import com.andi.storyapp.model.response.login.ResponseLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ):StoryRespons

    @GET("stories?location=1")
    suspend fun getStoriesMap(
        @Header("Authorization") token: String,
    ): Response<ResponseStories>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")name:String,
        @Field("email")email:String,
        @Field("password")password:String,
    ):Response<ResponseStatus>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")email:String,
        @Field("password")password:String,
    ):Response<ResponseLogin>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?=null,
        @Part("lon") lon: Float?=null,
    ): Response<ResponseStatus>
}