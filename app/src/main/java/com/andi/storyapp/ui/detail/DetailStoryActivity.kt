package com.andi.storyapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.MainActivity.Companion.DATA
import com.andi.storyapp.databinding.ActivityDetailBinding
import com.andi.storyapp.network.StoryResponeItem
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stori = intent?.getParcelableExtra<StoryResponeItem>(DATA)
        supportActionBar?.title = stori?.name
        binding.apply {
            tvName.text = stori?.name
            tvDes.text = stori?.description
        }
        Glide.with(this).load(stori?.photoUrl).into(binding.ivStory)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}