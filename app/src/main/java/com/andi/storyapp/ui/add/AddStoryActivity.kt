package com.andi.storyapp.ui.add

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andi.storyapp.adapter.AdapterStories
import com.andi.storyapp.databinding.ActivityAddStoryBinding
import com.andi.storyapp.databinding.ActivityMainBinding
import com.andi.storyapp.model.dummy.StoryDummy
import com.andi.storyapp.ui.detail.DetailStoryActivity

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}