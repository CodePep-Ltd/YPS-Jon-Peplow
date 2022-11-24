package com.codepep.yps.model

import com.codepep.yps.dto.RedditSubBottomLevelData

sealed class ViewModelState {
    object LOADING : ViewModelState()
    data class SUCCESS(val items: MutableList<RedditSubBottomLevelData>) : ViewModelState()
    data class FAILURE(val message: String?) : ViewModelState()
}