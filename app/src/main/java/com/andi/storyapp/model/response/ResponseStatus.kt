package com.andi.storyapp.model.response


import com.google.gson.annotations.SerializedName

data class ResponseStatus(
    @SerializedName("res")
    val res: ResponseStatusInner
){
    data class ResponseStatusInner(
        @SerializedName("error")
        val error: Boolean,
        @SerializedName("message")
        val message: String)
}