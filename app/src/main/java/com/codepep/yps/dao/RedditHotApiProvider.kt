package com.codepep.yps.dao

import com.codepep.yps.retrofit.RetrofitApi.getClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RedditHotApiProvider {
    @Provides
    @Singleton
    fun provideRedditHotTopics(): RedditHotApiInter = getClient().create(RedditHotApiInter::class.java)
}