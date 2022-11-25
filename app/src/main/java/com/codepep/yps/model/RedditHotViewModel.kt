package com.codepep.yps.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codepep.yps.dao.RedditHotRepository
import com.codepep.yps.model.datamanagment.RedditListDataManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RedditHotViewModel@Inject constructor(
private val repository: RedditHotRepository,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
private val dataManagement: RedditListDataManagement
): ViewModel() {
    val state = MutableStateFlow<ViewModelState>(ViewModelState.LOADING)
    private var hasLoaded = false

    init {
        loadHotTopics()
    }

    fun loadHotTopics() = viewModelScope.launch {
        if (hasLoaded) {
            if (dataManagement.hasReachedEnd()) {
                state.value = ViewModelState.SUCCESS(dataManagement.getFinalItems())
            } else {
                state.value = ViewModelState.LOADING
                state.value = ViewModelState.SUCCESS(dataManagement.getNextItems())
            }
        } else {
            try {
                val item = withContext(ioDispatcher) {
                    repository.fetchHotTopics()
                }
                val data = item.body()
                data?.let {
                    dataManagement.setMainItem(it)
                    state.value = ViewModelState.SUCCESS(dataManagement.getInitItems())
                    hasLoaded = true
                }?: run {
                    state.value = ViewModelState.FAILURE("Data is null!")
                }
            } catch (e: Exception) {
                state.value = ViewModelState.FAILURE(e.localizedMessage)
            }
        }
    }
}