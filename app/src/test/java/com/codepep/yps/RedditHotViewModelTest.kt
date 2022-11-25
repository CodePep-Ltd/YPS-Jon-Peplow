package com.codepep.yps

import com.codepep.yps.dao.RedditHotApiInter
import com.codepep.yps.dao.RedditHotRepository
import com.codepep.yps.dto.RedditTopLevelData
import com.codepep.yps.model.RedditHotViewModel
import com.codepep.yps.model.ViewModelState
import com.codepep.yps.model.datamanagment.RedditListDataManagement
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RedditHotViewModelTest {

    private val itemsPerPage = 5
    private val mockDataChildCount = 1

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: RedditHotRepository
    private lateinit var dataManagement: RedditListDataManagement
    private lateinit var mockData: RedditTopLevelData

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val mockApiInter = mock<RedditHotApiInter>()
        repository = RedditHotRepository(mockApiInter)
        dataManagement = RedditListDataManagement(itemsPerPage)
        mockData = MockRedditItemsDataProvider.getMockData(mockDataChildCount)
    }

    @Test
    fun `uses next data from list when previously loaded from api and not reached end of list`() = runTest {
        val mockResponse = mock<Response<RedditTopLevelData>>()
        whenever(mockResponse.body()).thenReturn(mockData)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)
        whenever(mockRepository.fetchHotTopics()).thenReturn(mockResponse)
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val dataResult = mockData.data.children.toMutableList()
        whenever(mockDataManagement.hasReachedEnd()).thenReturn(false)
        whenever(mockDataManagement.getNextItems()).thenReturn(dataResult)

        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)
        // Load the next page of results:
        viewModel.loadHotTopics()

        verify(mockDataManagement, times(1)).getNextItems()
        Assert.assertEquals("Success state not set!", ViewModelState.SUCCESS(dataResult), viewModel.state.value)
    }

    @Test
    fun `uses next data from list when previously loaded from api and reached end of list`() = runTest {
        val mockResponse = mock<Response<RedditTopLevelData>>()
        whenever(mockResponse.body()).thenReturn(mockData)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)
        whenever(mockRepository.fetchHotTopics()).thenReturn(mockResponse)
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val dataResult = mockData.data.children.toMutableList()
        whenever(mockDataManagement.hasReachedEnd()).thenReturn(true)
        whenever(mockDataManagement.getFinalItems()).thenReturn(dataResult)

        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)
        // Load the next page of results:
        viewModel.loadHotTopics()

        verify(mockDataManagement, times(1)).getFinalItems()
        Assert.assertEquals("Success state not set!", ViewModelState.SUCCESS(dataResult), viewModel.state.value)
    }

    @Test
    fun `loads data when first run`() = runTest {
        val mockResponse = mock<Response<RedditTopLevelData>>()
        whenever(mockResponse.body()).thenReturn(mockData)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)
        whenever(mockRepository.fetchHotTopics()).thenReturn(mockResponse)
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val dataResult = mockData.data.children.toMutableList()
        whenever(mockDataManagement.getInitItems()).thenReturn(dataResult)

        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)

        verify(mockRepository, times(1)).fetchHotTopics()
        verify(mockDataManagement, times(1)).setMainItem(mockData)
        verify(mockDataManagement, times(1)).getInitItems()
        Assert.assertEquals("Success state not set!", ViewModelState.SUCCESS(dataResult), viewModel.state.value)
    }

    @Test
    fun `error state hit when exception thrown`()  = runTest {
        whenever(repository.fetchHotTopics()).thenThrow(RuntimeException("Error"))
        val viewModel = RedditHotViewModel(repository, testDispatcher, dataManagement)
        Assert.assertEquals("Error state not set!", ViewModelState.FAILURE("Error"), viewModel.state.value)
    }

    @Test
    fun `error state hit when data returned is empty`()  = runTest {
        val response = mock<Response<RedditTopLevelData>>()
        whenever(repository.fetchHotTopics()).thenReturn(response)
        val viewModel = RedditHotViewModel(repository, testDispatcher, dataManagement)
        Assert.assertEquals("Error state not set!", ViewModelState.FAILURE("Data is null!"), viewModel.state.value)
    }
}