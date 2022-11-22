package com.codepep.yps.model

import com.codepep.yps.dto.RedditTopLevelData

sealed class ViewModelState {
    object START : ViewModelState()
    object LOADING : ViewModelState()
    data class SUCCESS(val item: RedditTopLevelData) : ViewModelState()
    data class FAILURE(val message: String?) : ViewModelState()
}