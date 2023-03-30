package com.andi.storyapp.model.response.login


import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: LoginResult,
    @SerializedName("message")
    val message: String
)