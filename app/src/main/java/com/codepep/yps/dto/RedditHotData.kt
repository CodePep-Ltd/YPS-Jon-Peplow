package com.codepep.yps.dto

import com.squareup.moshi.Json

data class RedditTopLevelData(
    @field:Json(name = "data")
    var data: RedditSubTopLevelData
)

data class RedditSubTopLevelData(
    @field:Json(name = "children")
    var children: List<RedditSubBottomLevelData>
)

data class RedditSubBottomLevelData(
    @field:Json(name = "data")
    var data: RedditHotChildData
)

data class RedditHotChildData(
    @field:Json(name = "title")
    var title: String?,
    @field:Json(name = "author")
    var author: String?,
    @field:Json(name = "url")
    var url: String?
)