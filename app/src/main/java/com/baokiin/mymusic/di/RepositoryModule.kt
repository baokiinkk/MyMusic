package com.baokiin.mymusic.di

import com.baokiin.mymusic.data.respository.Repository
import com.baokiin.mymusic.data.respository.RepositoryImpl
import com.baokiin.mymusic.data.respository.RepositoryLocal
import com.baokiin.mymusic.data.respository.RepositoryLocalImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun repository(repository: RepositoryImpl): Repository
    @Binds
    abstract fun repositoryLocal(repositorylocal: RepositoryLocalImpl): RepositoryLocal
}