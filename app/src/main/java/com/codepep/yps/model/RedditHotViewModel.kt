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
    val state = MutableStateFlow<ViewModelState>(ViewModelState.START)

    init {
        loadHotTopics()
    }

    private fun loadHotTopics() = viewModelScope.launch {
        state.value = ViewModelState.LOADING
        try {
            val item = withContext(Dispatchers.IO) { repository.fetchHotTopics() }
                state.value = ViewModelState.SUCCESS(item.body()!!)
        } catch (e: Exception) {
            state.value = ViewModelState.FAILURE(e.localizedMessage)
        }
    }
}