package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditTopLevelData

interface RedditListDataManInter {
    fun setMainItem(item: RedditTopLevelData)
    fun getInitItems(): MutableList<RedditSubBottomLevelData>
    fun getNextItems(): MutableList<RedditSubBottomLevelData>
    fun getFinalItems(): MutableList<RedditSubBottomLevelData>
    fun hasReachedEnd(): Boolean
}