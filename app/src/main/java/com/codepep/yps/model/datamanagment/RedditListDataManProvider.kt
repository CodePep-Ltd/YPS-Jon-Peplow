package com.codepep.yps.model.datamanagment

import com.codepep.yps.config.AppConfig.PAGE_ITEMS_COUNT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RedditListDataManProvider {
    @Provides
    @Singleton
    fun provideListDataManagement(): RedditListDataManagement =
        RedditListDataManagement(PAGE_ITEMS_COUNT)
}