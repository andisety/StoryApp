package com.andi.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andi.storyapp.network.StoryResponeItem

class PageDataSourceTest:PagingSource<Int,LiveData<List<StoryResponeItem>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponeItem>>>): Int? {
       return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponeItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<StoryResponeItem>): PagingData<StoryResponeItem> {
            return PagingData.from(items)
        }
    }
}