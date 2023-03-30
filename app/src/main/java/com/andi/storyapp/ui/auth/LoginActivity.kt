package com.andi.storyapp.ui.auth


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.MainActivity
import com.andi.storyapp.databinding.ActivityLoginBinding
import com.andi.storyapp.model.SavePrefObject
import com.andi.storyapp.model.response.login.ResponseLogin
import com.andi.storyapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.trim().toString()
            val password = binding.etPwd.text.trim().toString()


             if (email.isEmpty()){
                binding.etEmail.error = "Masukan Email"
            } else if (password.isEmpty()){
                binding.etPwd.error = "Masukan Password"
            }else{
                 isLoading = true
                 val client = ApiConfig.getApiService(this).login(email,password)
                 client.enqueue(object: Callback<ResponseLogin> {
                     override fun onResponse(
                         call: Call<ResponseLogin>,
                         response: Response<ResponseLogin>
                     ) {
                         if (response.isSuccessful) {
                             if (response.body()?.error!=true) {
                                 val token = response.body()?.loginResult?.token.toString()
                                 SavePrefObject.setToken(token,this@LoginActivity)
                                 isLoading = false
                                 goToMainActivity()
                             }
                         }
                     }

                     override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                         Log.e("LOGIN",t.message.toString())
                     }
                 })
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun goToMainActivity(){
        Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}