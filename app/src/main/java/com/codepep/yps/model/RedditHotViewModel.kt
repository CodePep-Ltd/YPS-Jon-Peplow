package com.codepep.yps.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codepep.yps.dao.RedditHotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RedditHotViewModel@Inject constructor(
private val repository: RedditHotRepository
): ViewModel() {
    val state = MutableStateFlow<ViewModelState>(ViewModelState.LOADING)
    private var hasLoaded = false
    private lateinit var pageManagement: RedditListDataManagement

    init {
        loadHotTopics()
    }

    fun loadHotTopics() = viewModelScope.launch {
        if (hasLoaded) {
            if (pageManagement.hasReachedEnd()) {
                state.value = ViewModelState.SUCCESS(pageManagement.getFinalItems())
            } else {
                state.value = ViewModelState.LOADING
                state.value = ViewModelState.SUCCESS(pageManagement.getNextItems())
            }
        } else {
            try {
                val item = withContext(Dispatchers.IO) { repository.fetchHotTopics() }
                val data = item.body()
                data?.let {
                    pageManagement =
                        RedditListDataManagement(PAGE_COUNT, it)
                    state.value = ViewModelState.SUCCESS(pageManagement.getInitItems())
                    hasLoaded = true
                }?: run {
                    state.value = ViewModelState.FAILURE("Data is null!")
                }
            } catch (e: Exception) {
                state.value = ViewModelState.FAILURE(e.localizedMessage)
            }
        }
    }

    companion object {
        const val PAGE_COUNT = 5
    }
}