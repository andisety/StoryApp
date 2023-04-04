package com.andi.storyapp.ui.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.model.response.ResponseStatus
import com.andi.storyapp.model.response.login.LoginResult
import com.andi.storyapp.network.ApiConfig
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel() {
    private val _responseLogin = MutableLiveData<ApiResult<LoginResult>>()
    val responseLogin : LiveData<ApiResult<LoginResult>> = _responseLogin
    private val _responeRegister = MutableLiveData<ApiResult<ResponseStatus.ResponseStatusInner>>()
    val responseRegister : LiveData<ApiResult<ResponseStatus.ResponseStatusInner>> = _responeRegister

    @SuppressLint("SuspiciousIndentation")
    fun login(email:String, password:String){
        viewModelScope.launch {
            _responseLogin.value = ApiResult.Loading
                try {
                    val response = ApiConfig.getApiService().login(email, password)
                    if(response.isSuccessful){
                        val result = response.body()
                        if (result != null) {
                            _responseLogin.value = ApiResult.Success(result.loginResult)
                        }
                    }else{
                        _responseLogin.value = ApiResult.Error("Error: ${response.code()}")
                    }
                }catch (e:Exception){
                    _responseLogin.value = ApiResult.Error(e.message.toString())
                }
        }

    }

    fun register(name:String,email: String,password: String){
       viewModelScope.launch {
           _responeRegister.value = ApiResult.Loading
           try {
               val response = ApiConfig.getApiService().register(name,email, password)
               if (response.isSuccessful){
                   val result = response.body()
                   if (result != null) {
                       _responeRegister.value = ApiResult.Success(result.res)
                   }
               }else{
                   _responeRegister.value = ApiResult.Error("Error:${response.code()}")
               }
           }catch (e:Exception){
               _responeRegister.value = ApiResult.Error(e.message.toString())
           }

       }


    }
}