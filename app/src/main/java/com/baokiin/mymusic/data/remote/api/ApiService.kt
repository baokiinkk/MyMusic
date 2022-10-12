package com.baokiin.mymusic.data.remote.api

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @GET("api/song/topSong/vietnam")
    suspend fun getTrending(): DataApi

    @GET("api/login")
    suspend fun login(): DataApi

    @GET("api/song/topSong/usuk")
    suspend fun getTopAmerica(): DataApi

    @GET("api/song/topSong/hanquoc")
    suspend fun getTopKpop(): DataApi

    @GET("api/song/topSong/vietnam")
    suspend fun getTopVpop(): DataApi

    @GET("api/favourite")
    suspend fun getSongsLiked(@Header("Authorization") accessToken: String): DataApi

    @POST("api/favourite")
    @Multipart
    suspend fun likeSong(
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody
    ): DataApi

    @DELETE("api/favourite/{id}")
    suspend fun unLikeSong(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): DataApi


    @Streaming
    @GET
    suspend fun downloadMusic(@Url url: String): ResponseBody

    @Streaming
    @GET
    suspend fun downloadLyric(@Url url: String): ResponseBody

    @Streaming
    @GET
    suspend fun downloadImg(@Url url: String): ResponseBody


}

interface FindMusicService {
    @GET("complete?type=artist,song,key,code&num=50")
    suspend fun getSong(@Query("query") id: String): DataFind

}