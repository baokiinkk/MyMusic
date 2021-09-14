package com.baokiin.mymusic.di

import android.content.Context
import androidx.room.Room
import com.baokiin.mymusic.data.local.AppDao
import com.baokiin.mymusic.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "AppDataBase").build()

    @Singleton
    @Provides
    fun providesRoom(database: AppDatabase):AppDao = database.appDao()

}