package com.andi.storyapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.model.response.ResponseStatus
import com.andi.storyapp.model.response.Story
import com.andi.storyapp.network.ApiConfig
import com.andi.storyapp.preference.PreferenceLogin
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

@SuppressLint("StaticFieldLeak")
class StoryViewModel(application: Application):AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val _storiesV2 = MutableLiveData<ApiResult<List<Story>>>()
    val storiesV2 : LiveData<ApiResult<List<Story>>> = _storiesV2
    private val _responseUpload = MutableLiveData<ApiResult<ResponseStatus.ResponseStatusInner>>()
    val responseUpload : LiveData<ApiResult<ResponseStatus.ResponseStatusInner>> = _responseUpload

    init {
        getStoriesV2()
    }

    fun getStoriesV2(){
        viewModelScope.launch {
            _storiesV2.value = ApiResult.Loading
            val token = PreferenceLogin(context).getToken()
            try {
                val response = ApiConfig.getApiService().getStoriesMap(token = "Bearer $token")
                if (response.isSuccessful){
                    val result = response.body()
                    if (result != null) {
                        _storiesV2.value = ApiResult.Success(result.listStory)
                    }
                }else{
                    _storiesV2.value = ApiResult.Error("Error: ${response.code()}")
                }
            }catch (e:Exception){
                _storiesV2.value = ApiResult.Error(e.message.toString())
            }
        }
    }

    fun uploadStory(token:String,file:MultipartBody.Part,description:RequestBody,lat:Float?,lon:Float?){
        viewModelScope.launch {
            _responseUpload.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().uploadImage(token,file,description,lat,lon)
                if (response.isSuccessful){
                    val result = response.body()
                    if (result!=null){
                        _responseUpload.value = ApiResult.Success(result.res)
                    }
                }else{
                    _responseUpload.value = ApiResult.Error("Error: ${response.code()}")
                }
            }catch (e:Exception){
                _responseUpload.value = ApiResult.Error(e.message.toString())
            }
        }

    }
}