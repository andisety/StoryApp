package com.andi.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.network.ApiConfig
import com.andi.storyapp.MainActivity
import com.andi.storyapp.databinding.ActivityRegisterBinding
import com.andi.storyapp.model.response.ResponseStatus
import com.andi.storyapp.model.response.Wrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        binding.btnRegister.setOnClickListener {
            val name = binding.etname.text.trim().toString()
            val email = binding.etEmail.text.trim().toString()
            val password = binding.etPwd.text.trim().toString()
            if (name.isEmpty()){
                binding.etname.error = "Masukan Nama"
            }else if (email.isEmpty()){
                binding.etEmail.error = "Masukan Email"
            } else if (password.isEmpty()){
                binding.etPwd.error = "Masukan Password"
            }else{
//                register(name, email, password)
                isLoading = true
                val client = ApiConfig.getApiService(this).register(name,email,password)
                client.enqueue(object: Callback<Wrapper<ResponseStatus>> {
                    override fun onResponse(
                        call: Call<Wrapper<ResponseStatus>>,
                        response: Response<Wrapper<ResponseStatus>>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()?.status?.error==false){
                                Log.e("REGISTER","SUCCESS")
                                isLoading = false
                                toLoginActivity()

                            }else{
                                Log.e("REGISTER",response.message())
                                isLoading = false
                            }
                        }else{
                            Log.e("REGISTER",response.message())
                            isLoading = false
                        }
                    }

                    override fun onFailure(call: Call<Wrapper<ResponseStatus>>, t: Throwable) {
                        Log.e("REGISTER",t.message.toString())
                        isLoading = false
                    }
                })


            }
        }

        showLoading(isLoading)

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun toLoginActivity(){
        Toast.makeText(this,"Register Success",Toast.LENGTH_SHORT).show()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }


}