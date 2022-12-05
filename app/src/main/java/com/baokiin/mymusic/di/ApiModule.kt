package com.baokiin.mymusic.di

import com.baokiin.mymusic.data.remote.api.ApiService
import com.baokiin.mymusic.data.remote.api.FindMusicService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(factory: Gson, client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://146.190.100.201/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideRetrofit2(factory: Gson, client: OkHttpClient): FindMusicService {
        return Retrofit.Builder()
            .baseUrl("https://ac.mp3.zing.vn/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
            .create(FindMusicService::class.java)
    }

}