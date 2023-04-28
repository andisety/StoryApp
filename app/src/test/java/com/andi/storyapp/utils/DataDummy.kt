package com.andi.storyapp.utils

import com.andi.storyapp.network.StoryResponeItem

object DataDummy {
    fun generateData(): List<StoryResponeItem> {
        val storyList = ArrayList<StoryResponeItem>()
        for(i in 0..10){
            val story = StoryResponeItem(
                "2022-01-08T06:34:18.598Z",
                "Lorem Ipsum",
                "story-FvU4u0Vp2S3PMsFg$i",
                -10.212,
                -16.002,
                "Dimas",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png"
            )
            storyList.add(story)
        }
        return storyList
    }
    fun generateDataNul(): List<StoryResponeItem> {
        return emptyList()
    }
}