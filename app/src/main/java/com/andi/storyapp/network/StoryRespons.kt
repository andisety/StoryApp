package com.andi.storyapp.network


import com.google.gson.annotations.SerializedName

data class StoryRespons(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<StoryResponeItem>,
    @SerializedName("message")
    val message: String
)