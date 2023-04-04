package com.andi.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.MainActivity
import com.andi.storyapp.databinding.ActivityLoginBinding
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.model.response.login.LoginResult
import com.andi.storyapp.preference.PreferenceLogin

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private val authViewModel:AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        authViewModel.responseLogin.observe(this){login->
            when(login){
                is ApiResult.Success->{
                    saveToken(login.data)
                    showLoading(false)
                }
                is ApiResult.Error->{
                    showDialogEror()
                    showLoading(false)
                }
                is ApiResult.Loading->{
                    showLoading(true)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.trim().toString()
            val password = binding.etPwd.text?.trim().toString()

             if (email.isEmpty()){
                binding.etEmail.error = "Masukan Email"
            } else if (password.isEmpty()){
                binding.etPwd.error = "Masukan Password"
            }else if(password.length<8){
                binding.etPwd.error = "Inputan harus lebih dari 8 karakter"
                 binding.etPwd.requestFocus()
             }else{
                 authViewModel.login(email, password)
             }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.btnLogin, View.SCALE_X, 1f, 1.2f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.btnLogin, View.SCALE_Y, 1f, 1.2f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginTextVIew = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val descTextVIew = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)

        val labelEmail = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(500)
        val labelPwd = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(500)

        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val etPwd = ObjectAnimator.ofFloat(binding.etPwd, View.ALPHA, 1f).setDuration(500)

        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        val togetherLogin = AnimatorSet().apply {
            playTogether(loginTextVIew,descTextVIew)
        }
        val togetherEmail = AnimatorSet().apply {
            playTogether(labelEmail,etEmail)
        }
        val togetherPwd = AnimatorSet().apply {
            playTogether(labelPwd,etPwd)
        }
        AnimatorSet().apply {
            playSequentially(togetherLogin,togetherEmail,togetherPwd,btnLogin)
            start()
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
    private fun showDialogEror() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Login Error, silahkan coba lagi !")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
        }
        builder.show()

    }

    private fun saveToken(loginResult: LoginResult){
            val prefLogin = PreferenceLogin(this)
            prefLogin.setToken(loginResult.token)
            goToMainActivity()

    }

}