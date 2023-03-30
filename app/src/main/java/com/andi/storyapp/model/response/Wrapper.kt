package com.andi.storyapp.model.response

import com.google.gson.annotations.SerializedName

data class Wrapper<T> (
    @SerializedName("status")
    val status: ResponseStatus?=null,
    @SerializedName("data")
    val data: T?=null,

)