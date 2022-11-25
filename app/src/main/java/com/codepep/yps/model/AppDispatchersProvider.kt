package com.codepep.yps.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDispatchersProvider {
    @Provides
    @Singleton
    fun provideAppDispatcher(): CoroutineDispatcher = Dispatchers.IO
}