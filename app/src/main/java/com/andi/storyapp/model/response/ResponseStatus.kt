package com.andi.storyapp.model.response


import com.google.gson.annotations.SerializedName

data class ResponseStatus(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)