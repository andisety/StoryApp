package com.andi.storyapp.model.response

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val errorMessage: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}
