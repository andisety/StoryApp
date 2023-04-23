package com.andi.storyapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
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
import com.andi.storyapp.adapter.LoadingStateAdapter
import com.andi.storyapp.adapter.StoryListAdapter
import com.andi.storyapp.databinding.ActivityMainBinding
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.model.response.StoryMap
import com.andi.storyapp.network.StoryResponeItem
import com.andi.storyapp.preference.PreferenceLogin
import com.andi.storyapp.ui.add.AddStoryActivity
import com.andi.storyapp.ui.auth.LoginActivity
import com.andi.storyapp.ui.detail.DetailStoryActivity
import com.andi.storyapp.ui.map.StoryMapsActivity
import com.andi.storyapp.ui.viewmodel.StoryViewModel
import com.andi.storyapp.ui.viewmodel.ViewModelFactory
import com.andi.storyapp.ui.viewmodel.ViewModelLocal

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private val listStory = arrayListOf<StoryMap>()
    private val viewModelLocal:ViewModelLocal by viewModels{
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         prefLogin = PreferenceLogin(this)
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()){
            val intent = Intent(this,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        storyViewModel.storiesV2.observe(this){stories->
            when(stories){
                is ApiResult.Success->{
                    showLoading(false)
                    val story = stories.data
                    listStory.clear()
                    for (i in story.indices){
                        val storyMap = StoryMap( story[i].name, story[i].lat, story[i].lon)
                        listStory.add(storyMap)
                    }

                }
                is ApiResult.Error->{
                    showLoading(false)
                    Toast.makeText(this,stories.errorMessage,Toast.LENGTH_SHORT).show()
                }
                is ApiResult.Loading->{
                    showLoading(true)
                }
            }
        }
        getData()
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
        getData()
        storyViewModel.getStoriesV2()
    }
    private fun getData() {
        val adapter = StoryListAdapter(object:StoryListAdapter.StoriesListener{
            override fun onKlik(story: StoryResponeItem) {
                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                intent.putExtra(DATA,story)
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
        })
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rcList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModelLocal.story.observe(this) {
            adapter.submitData(lifecycle, it)
            binding.rcList.adapter = adapter
            binding.rcList.layoutManager = layoutManager
        }
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
            R.id.menu_setting_language->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu_story_map->{
                val intent = Intent(this@MainActivity, StoryMapsActivity::class.java)
                intent.putExtra(DATA,listStory)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ResourceType")
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.logout))
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
        Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()
    }

    companion object{
        lateinit var prefLogin : PreferenceLogin
        const val DATA = "DATA"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}