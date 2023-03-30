package com.andi.storyapp.model

 import android.content.Context
 import android.util.Log
 import androidx.lifecycle.LiveData
 import androidx.lifecycle.MutableLiveData
 import androidx.lifecycle.ViewModel
 import com.andi.storyapp.model.response.ResponseStories
 import com.andi.storyapp.network.ApiConfig
 import retrofit2.Call
 import retrofit2.Callback
 import retrofit2.Response

class MainViewModel:ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _stories = MutableLiveData<ResponseStories>()
    val stories: LiveData<ResponseStories> = _stories


     fun getStories(context: Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getStories()
        client.enqueue(object : Callback<ResponseStories>{
            override fun onResponse(
                call: Call<ResponseStories>,
                response: Response<ResponseStories>
            ) {
                if (response.isSuccessful){
                    _stories.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ResponseStories>, t: Throwable) {
                Log.e("GETSTORIES",t.message.toString())
                _isLoading.value = false
            }
        })

    }



}