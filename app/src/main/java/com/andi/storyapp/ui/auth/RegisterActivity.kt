package com.andi.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.databinding.ActivityRegisterBinding
import com.andi.storyapp.model.response.ApiResult

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnRegister.setOnClickListener {
            val name = binding.etname.text.trim().toString()
            val email = binding.etEmail.text?.trim().toString()
            val password = binding.etPwd.text?.trim().toString()
            if (name.isEmpty()) {
                binding.etname.error = "Masukan Nama"
            } else if (email.isEmpty()) {
                binding.etEmail.error = "Masukan Email"
            } else if (password.isEmpty()) {
                binding.etPwd.error = "Masukan Password"
            } else if(password.length<8){
                binding.etPwd.error = "Inputan harus lebih dari 8 karakter"
                binding.etPwd.requestFocus()
            }else {
                authViewModel.register(name, email, password)
            }
        }
        authViewModel.responseRegister.observe(this){register->
            when(register){
                is ApiResult.Success->{
                    showLoading(false)
                    showDialog(SUCCESS)
                }
                is ApiResult.Error->{
                    showLoading(false)
                    showDialog(ERROR)
                }
                is ApiResult.Loading->{
                    showLoading(true)
                }
            }
        }

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun toLoginActivity() {
        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showDialog(mode: String) {
        val builder = AlertDialog.Builder(this)
        if (mode == ERROR) {
            val title = ERROR
            val message = "Register erorr, silahkan coba lagi"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
            }
            builder.show()
        } else if (mode == "success") {
            val title = SUCCESS
            val message = "Register success, silahkan login !"
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                toLoginActivity()
            }
            builder.show()
        }




    }

    companion object {
        const val ERROR = "error"
        const val SUCCESS = "success"

    }
}