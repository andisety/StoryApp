package com.andi.storyapp.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.MainActivity
import com.andi.storyapp.MainActivity.Companion.DATA
import com.andi.storyapp.databinding.ActivityDetailBinding
import com.andi.storyapp.databinding.ActivityLoginBinding
import com.andi.storyapp.model.dummy.StoryDummy
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stori = intent?.getParcelableExtra<StoryDummy>(DATA)
        supportActionBar?.title = stori?.name
        binding.apply {
            tvName.text = stori?.name
            tvDes.text = stori?.description
        }
        Glide.with(this).load(stori?.photoUrl).into(binding.ivStory)
    }

}