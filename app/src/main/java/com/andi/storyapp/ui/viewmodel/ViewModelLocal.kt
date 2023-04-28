package com.andi.storyapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andi.storyapp.data.StoryRepository
import com.andi.storyapp.di.Injection
import com.andi.storyapp.network.StoryResponeItem

class ViewModelLocal(val storyRepository:StoryRepository):ViewModel() {
    val story:LiveData<PagingData<StoryResponeItem>> by lazy {  storyRepository.getStory().cachedIn(viewModelScope) }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelLocal::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelLocal(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}