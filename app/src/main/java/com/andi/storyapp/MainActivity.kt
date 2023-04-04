package com.andi.storyapp

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.andi.storyapp.adapter.AdapterStories
import com.andi.storyapp.databinding.ActivityMainBinding
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.model.response.Story
import com.andi.storyapp.preference.PreferenceLogin
import com.andi.storyapp.ui.add.AddStoryActivity
import com.andi.storyapp.ui.auth.LoginActivity
import com.andi.storyapp.ui.detail.DetailStoryActivity
import com.andi.storyapp.ui.viewmodel.StoryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val storyViewModel:StoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefLogin = PreferenceLogin(this)
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()){
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        storyViewModel.storiesV2.observe(this){stories->
            when(stories){
                is ApiResult.Success->{
                    setupList(stories.data)
                    showLoading(false)
                }
                is ApiResult.Error->{
                    Log.e("STORIES",stories.errorMessage)
                    showLoading(false)
                }
                is ApiResult.Loading->{
                    showLoading(true)
                }
            }
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this,AddStoryActivity::class.java),ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.getStoriesV2()
    }

    private fun setupList(listStori:List<Story>) {
        val adapterStori = AdapterStories(listStori,this,object : AdapterStories.StoriesListener{
            override fun onKlik(story: Story) {
                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                intent.putExtra(DATA,story)
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
        })
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rcList.adapter = adapterStori
        binding.rcList.layoutManager = layoutManager

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           R.id.menu_logout->{
               showDialog()
           }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Apakah yakin ingin logout?")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            logout()
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_CODE_PERMISSIONS) {
                if (!allPermissionsGranted()) {
                    Toast.makeText(
                        this,
                        "Tidak mendapatkan permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun logout(){
       val prefLogin = PreferenceLogin(this)
        prefLogin.clearToken()
        val intent = Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object{
        const val DATA = "DATA"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}