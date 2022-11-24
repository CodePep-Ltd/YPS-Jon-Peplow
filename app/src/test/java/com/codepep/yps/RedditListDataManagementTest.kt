package com.codepep.yps

import com.codepep.yps.dto.RedditHotChildData
import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditSubTopLevelData
import com.codepep.yps.dto.RedditTopLevelData
import com.codepep.yps.model.RedditListDataManagement
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RedditListDataManagementTest {
    private val pageCount = 5
    private val mockDataChildCount = 20
    private val mockData: RedditTopLevelData = getMockData()

    @Test
    fun `init items return paged requested amount`() {
        val manager = RedditListDataManagement(pageCount, mockData)
        assertEquals("Unexpected initial count of items returned", pageCount, manager.getInitItems().size)
    }

    @Test
    fun `next items return expected amount on one page increment`() {
        val manager = RedditListDataManagement(pageCount, mockData)
        val items = manager.getNextItems()
        assertEquals("Unexpected initial count of items returned", 2 * pageCount, items.size)
    }

    @Test
    fun `next items return max amount on max amount of page increments`() {
        val manager = RedditListDataManagement(pageCount, mockData)
        for (i in 1..mockDataChildCount + 1) {
            manager.getNextItems()
        }
        assertEquals("End of items not reached. Update test!", true, manager.hasReachedEnd())
        val items = manager.getFinalItems()
        assertEquals("Unexpected initial count of items returned", mockDataChildCount, items.size)
    }

    private fun getMockData(): RedditTopLevelData {
        val childrenList = ArrayList<RedditSubBottomLevelData>()
        for (i in 1..mockDataChildCount) {
            childrenList.add(RedditSubBottomLevelData(data = RedditHotChildData(
                title = "name$i" ,
                author = "author$i",
                url = "url$i",
                thumbnail = "thumbnail$i"
            )))
        }
        return RedditTopLevelData(
            data = RedditSubTopLevelData(
                children = childrenList
            )
        )
    }
}