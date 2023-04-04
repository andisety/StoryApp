package com.andi.storyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andi.storyapp.databinding.ItemStoryBinding
import com.andi.storyapp.model.response.Story
import com.bumptech.glide.Glide

class AdapterStories(
    private val listStories:List<Story>,
    val context: Context,
    private val listener:StoriesListener
): RecyclerView.Adapter<AdapterStories.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStories[position]
        holder.binding.apply {
            tvName.text = story.name
            tvDes.text = story.description

            Glide.with(context)
                .load(story.photoUrl)
                .into(ivStory)
        }
        holder.itemView.setOnClickListener {
            listener.onKlik(story)
        }

    }

    override fun getItemCount(): Int {
        return listStories.size
    }


    class ViewHolder(val binding:ItemStoryBinding):RecyclerView.ViewHolder(binding.root)

    interface StoriesListener{
        fun onKlik(story: Story)
    }
}