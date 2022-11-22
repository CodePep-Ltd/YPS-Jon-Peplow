package com.codepep.yps.dao

import com.codepep.yps.dto.RedditTopLevelData
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Response
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class RedditHotRepository @Inject constructor(
    private val redditHotDao: RedditHotApiInter
) {
    suspend fun fetchHotTopics(): Response<RedditTopLevelData> = redditHotDao.getHotTopics()
}