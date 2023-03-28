package com.andi.storyapp.model.response


import com.google.gson.annotations.SerializedName

data class ResponseStories(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)