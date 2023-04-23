package com.andi.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andi.storyapp.databinding.ItemStoryBinding
import com.andi.storyapp.network.StoryResponeItem
import com.bumptech.glide.Glide

class StoryListAdapter(private val listener:StoriesListener):PagingDataAdapter<StoryResponeItem,StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                listener.onKlik(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    class MyViewHolder(private val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root ){
        fun bind(data: StoryResponeItem) {
            binding.apply {
                tvName.text = data.name
                tvDes.text = data.description

                Glide.with(this.ivStory.context)
                    .load(data.photoUrl)
                    .into(ivStory)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponeItem>() {

            override fun areItemsTheSame(oldItem: StoryResponeItem, newItem: StoryResponeItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponeItem, newItem: StoryResponeItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface StoriesListener{
        fun onKlik(story: StoryResponeItem)
    }
}