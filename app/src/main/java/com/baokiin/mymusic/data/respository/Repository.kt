package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import okhttp3.ResponseBody
import javax.inject.Singleton


@Singleton
interface Repository {
    suspend fun getTrending(): DataApi
    suspend fun login(): DataApi
    suspend fun getTopAmerica(): DataApi
    suspend fun getTopKpop(): DataApi
    suspend fun getTopVpop(): DataApi
    suspend fun getSongsLiked():DataApi
    suspend fun search(id:String): DataFind
    suspend fun likeSong(token:String,idSong:String): DataApi
    suspend fun unLikeSong(token:String,idSong:String): DataApi
    suspend fun downloadMusic(url:String):ResponseBody
    suspend fun downloadLyric(url:String):ResponseBody
    suspend fun downloadImg(url:String):ResponseBody
}