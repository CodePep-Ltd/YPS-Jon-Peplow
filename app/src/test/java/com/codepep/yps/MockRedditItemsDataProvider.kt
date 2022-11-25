package com.codepep.yps

import com.codepep.yps.dto.RedditHotChildData
import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditSubTopLevelData
import com.codepep.yps.dto.RedditTopLevelData

class MockRedditItemsDataProvider {
    companion object {
        fun getMockData(count: Int): RedditTopLevelData {
            val childrenList = ArrayList<RedditSubBottomLevelData>()
            for (i in 1..count) {
                childrenList.add(RedditSubBottomLevelData(data = RedditHotChildData(
                    title = "name$i" ,
                    author = "author$i",
                    url = "url$i",
                    thumbnail = "thumbnail$i"
                )
                ))
            }
            return RedditTopLevelData(
                data = RedditSubTopLevelData(
                    children = childrenList
                )
            )
        }
    }
}