package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import javax.inject.Singleton


@Singleton
interface Repository {
    suspend fun getTrending(): DataApi
    suspend fun getTopAmerica(): DataApi
    suspend fun getTopKpop(): DataApi
    suspend fun getTopVpop(): DataApi
    suspend fun getSongs(id:String):DataApi
    suspend fun search(id:String): DataFind
}