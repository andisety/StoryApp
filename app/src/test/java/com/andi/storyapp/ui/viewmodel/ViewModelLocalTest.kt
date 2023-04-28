package com.andi.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.andi.storyapp.adapter.StoryListAdapter
import com.andi.storyapp.data.StoryRepository
import com.andi.storyapp.network.StoryResponeItem
import com.andi.storyapp.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelLocalTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var viewModelLocal: ViewModelLocal
    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        viewModelLocal = ViewModelLocal(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Data  Not Null `(): Unit = runTest {
        val dataDummy = DataDummy.generateData()
        val data = PageDataSourceTest.snapshot(dataDummy)
        val expectedStory = MutableLiveData<PagingData<StoryResponeItem>>()
        expectedStory.value = data
        `when`(repository.getStory()).thenReturn(expectedStory)
        val actualStories = repository.getStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = TestListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
       differ.submitData(actualStories)
        verify(repository).getStory()
        assertNotNull(differ.snapshot())
        assertEquals(dataDummy,differ.snapshot())
        assertEquals(dataDummy[0],differ.snapshot()[0])
        assertEquals(dataDummy.size,differ.snapshot().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Data  Null `(): Unit = runTest {
        val dataDummy = DataDummy.generateDataNul()
        val data = PageDataSourceTest.snapshot(dataDummy)
        val expectedStory = MutableLiveData<PagingData<StoryResponeItem>>()
        expectedStory.value = data
        `when`(repository.getStory()).thenReturn(expectedStory)
        val actualStories = repository.getStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = TestListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
       differ.submitData(actualStories)
        verify(repository).getStory()
        assertEquals(dataDummy.size,differ.snapshot().size)
    }




}