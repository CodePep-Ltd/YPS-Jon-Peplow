package com.codepep.yps

import com.codepep.yps.dao.RedditHotApiInter
import com.codepep.yps.dao.RedditHotRepository
import com.codepep.yps.dto.RedditTopLevelData
import com.codepep.yps.model.RedditHotViewModel
import com.codepep.yps.model.ViewModelState
import com.codepep.yps.model.datamanagment.RedditListDataManagement
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RedditHotViewModelTest {

    private val itemsPerPage = 5

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: RedditHotRepository
    private lateinit var dataManagement: RedditListDataManagement

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val mockApiInter = mock<RedditHotApiInter>()
        repository = RedditHotRepository(mockApiInter)
        dataManagement = RedditListDataManagement(itemsPerPage)
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