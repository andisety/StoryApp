package com.andi.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.andi.storyapp.database.StoryDataBase
import com.andi.storyapp.network.ApiService
import com.andi.storyapp.network.StoryResponeItem

class StoryRepository(private val storyDatabase:StoryDataBase,private val apiService: ApiService,private val token:String) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory():LiveData<PagingData<StoryResponeItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllQuote()
            }
        ).liveData
    }

}