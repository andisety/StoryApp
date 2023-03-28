package com.andi.storyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andi.storyapp.adapter.AdapterStories
import com.andi.storyapp.databinding.ActivityMainBinding
import com.andi.storyapp.model.dummy.StoryDummy
import com.andi.storyapp.ui.add.AddStoryActivity
import com.andi.storyapp.ui.detail.DetailStoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listStory = listOf(
            StoryDummy("hdjsdhhdjdhjhsd dsdbjadj djsdjadj jsdhjdad sjhdjdj","1","Andi Setyo Purnomo","https://awsimages.detik.net.id/visual/2018/09/07/5560949f-3926-492d-badc-3ed796c2a85c.jpeg"),
            StoryDummy("hdjsdhhdjdhjhsd dsdbjadj djsdjadj jsdhjdad sjhdjdj","2","Andi  ","https://awsimages.detik.net.id/visual/2018/09/07/5560949f-3926-492d-badc-3ed796c2a85c.jpeg"),
            StoryDummy("hdjsdhhdjdhjhsd dsdbjadj djsdjadj jsdhjdad sjhdjdj","3","Andi Setyo ","https://awsimages.detik.net.id/visual/2018/09/07/5560949f-3926-492d-badc-3ed796c2a85c.jpeg"),
            StoryDummy("hdjsdhhdjdhjhsd dsdbjadj djsdjadj jsdhjdad sjhdjdj","4","Andi Setyo P","https://awsimages.detik.net.id/visual/2018/09/07/5560949f-3926-492d-badc-3ed796c2a85c.jpeg"),
        )
        setupList(listStory)

        binding.fab.setOnClickListener {
            startActivity(Intent(this,AddStoryActivity::class.java))
        }



    }

    private fun setupList(listStori:List<StoryDummy>) {
        val adapterStori = AdapterStories(listStori,this,object : AdapterStories.StoriesListener{
            override fun onKlik(story: StoryDummy) {
                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                intent.putExtra(DATA,story)
                startActivity(intent)
            }
        })
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rcList.adapter = adapterStori
        binding.rcList.layoutManager = layoutManager

    }
    companion object{
        const val DATA = "DATA"
    }
}