package com.andi.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.andi.storyapp.data.StoryRepository
import com.andi.storyapp.network.StoryResponeItem
import com.andi.storyapp.utils.DataDummy
import com.andi.storyapp.utils.TestDiffCallback
import com.andi.storyapp.utils.TestListCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelLocalTest{
    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModelLocal: ViewModelLocal
    private val dummyStories = DataDummy.generateData()
    @Mock
    private lateinit var repository: StoryRepository
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        viewModelLocal = ViewModelLocal(repository)
    }


    @Test
    fun `when Get Data  Not Null `(): Unit = runBlocking {
            val observer = Observer<PagingData<StoryResponeItem>> {}
            try{
                val data = PagingData.from(dummyStories)
                val expectedStory = MutableLiveData<PagingData<StoryResponeItem>>()
                expectedStory.value = data
                `when`(repository.getStory()).thenReturn(expectedStory)
                    launch {
                    viewModelLocal.story.observeForever { page->
                        val differ = AsyncPagingDataDiffer(
                            diffCallback = TestDiffCallback<StoryResponeItem>(),
                            updateCallback = TestListCallback(),
                            workerDispatcher = Dispatchers.Main
                        )
                        this.launch {
                            differ.submitData(page)
                        }
                        assertNotNull(differ.snapshot())
                        assertEquals(dummyStories.size,differ.snapshot().size)
                        assertEquals(dummyStories[0],differ.snapshot()[0])
                    }
                }
            }finally {
                viewModelLocal.story.removeObserver(observer)
            }
    }

    @Test
    fun `when gGt Data Null`() : Unit = runBlocking {
        val observer = Observer<PagingData<StoryResponeItem>> {}
        try{
            val data = PagingData.from(dummyStories)
            val expectedStory = MutableLiveData<PagingData<StoryResponeItem>>()
            expectedStory.value = data
            `when`(repository.getStory()).thenReturn(expectedStory)
            launch {
                viewModelLocal.story.observeForever { page->
                    val differ = AsyncPagingDataDiffer(
                        diffCallback = TestDiffCallback<StoryResponeItem>(),
                        updateCallback = TestListCallback(),
                        workerDispatcher = Dispatchers.Main
                    )
                    this.launch {
                        differ.submitData(page)
                    }
                    assertEquals(0,differ.itemCount)
                }
            }
        }finally {
            viewModelLocal.story.removeObserver(observer)
        }
    }
}