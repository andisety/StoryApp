package com.andi.storyapp.network

import com.andi.storyapp.model.request.LoginRequest
import com.andi.storyapp.model.response.ResponseStatus
import com.andi.storyapp.model.response.ResponseStories
import com.andi.storyapp.model.response.Wrapper
import com.andi.storyapp.model.response.login.ResponseLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("stories")
    fun getStories():Call<ResponseStories>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name")name:String,
        @Field("email")email:String,
        @Field("password")password:String,
    ):Call<Wrapper<ResponseStatus>>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")email:String,
        @Field("password")password:String,
    ):Call<ResponseLogin>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ResponseStatus>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<ResponseLogin>

}