package com.andi.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andi.storyapp.network.StoryResponeItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories:List<StoryResponeItem>)
    @Query("SELECT * FROM story")
    fun getAllQuote(): PagingSource<Int, StoryResponeItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}