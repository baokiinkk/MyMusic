package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import com.baokiin.mymusic.data.model.DataPlayListApi
import com.baokiin.mymusic.data.model.UserStatus
import okhttp3.ResponseBody
import javax.inject.Singleton


@Singleton
interface Repository {
    suspend fun getTrending(): DataApi
    suspend fun getTopAmerica(): DataApi
    suspend fun getTopKpop(): DataApi
    suspend fun getTopVpop(): DataApi
    suspend fun getSongsLiked(): DataApi
    suspend fun search(id: String): DataFind
    suspend fun likeSong(token: String, idSong: String): DataApi
    suspend fun createPlayList(token: String?, name: String): DataApi
    suspend fun addSongPlayList(playlistId: String,songId:String): DataApi
    suspend fun getPublicPlayList(): DataPlayListApi
    suspend fun getPrivatePlayList(): DataPlayListApi
    suspend fun unLikeSong(token: String, idSong: String): ResponseBody
    suspend fun downloadMusic(url: String?): ResponseBody
    suspend fun downloadLyric(url: String): ResponseBody
    suspend fun downloadImg(url: String): ResponseBody
    suspend fun checkUser(token: String): UserStatus
}