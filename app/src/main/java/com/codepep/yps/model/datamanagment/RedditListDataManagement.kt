package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditTopLevelData

class RedditListDataManagement(
    private val itemsPerPage: Int
) : RedditListDataManInter {
    private var page: Int = 1
    private var hasReachedEnd: Boolean = false
    private var listItems: MutableList<RedditSubBottomLevelData> = ArrayList()
    private lateinit var mainItem: RedditTopLevelData

    override fun setMainItem(item: RedditTopLevelData) {
        page = 1
        mainItem = item
    }

    override fun getInitItems(): MutableList<RedditSubBottomLevelData> {
        for (i in 1.rangeTo(itemsPerPage)) {
            listItems.add(mainItem.data.children[i-1])
        }
        return listItems
    }

    override fun getNextItems(): MutableList<RedditSubBottomLevelData> {
        if (hasReachedEnd) {
            return listItems
        }
        page++
        var nextAmount = page * itemsPerPage
        if (nextAmount > mainItem.data.children.size) {
            hasReachedEnd = true
            nextAmount = mainItem.data.children.size
        }
        listItems.clear()
        for (i in 1.rangeTo(nextAmount)) {
            listItems.add(mainItem.data.children[i-1])
        }
        return listItems
    }

    override fun getFinalItems(): MutableList<RedditSubBottomLevelData> = listItems

    override fun hasReachedEnd(): Boolean = hasReachedEnd
}