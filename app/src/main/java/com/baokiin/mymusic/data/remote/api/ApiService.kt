package com.baokiin.mymusic.data.remote.api

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import com.baokiin.mymusic.data.model.DataPlayListApi
import com.baokiin.mymusic.data.model.UserStatus
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @GET("api/song/topSong/vietnam")
    suspend fun getTrending(): DataApi

    @GET("api/song/topSong/usuk")
    suspend fun getTopAmerica(): DataApi

    @GET("api/song/topSong/hanquoc")
    suspend fun getTopKpop(): DataApi

    @GET("api/song/topSong/vietnam")
    suspend fun getTopVpop(): DataApi

    @GET("api/song/search")
    suspend fun search(@Query("keyword")key:String): DataApi

    @GET("api/favourite")
    suspend fun getSongsLiked(@Header("Authorization") accessToken: String): DataApi

    @GET("api/playlist/public")
    suspend fun getPublicPlayList(): DataPlayListApi

    @GET("api/user/check-user")
    suspend fun checkUser(@Header("Authorization") accessToken: String): UserStatus

    @GET("api/playlist/myPlaylist")
    suspend fun getMyPlayList(@Header("Authorization") accessToken: String): DataPlayListApi

    @POST("api/favourite")
    @Multipart
    suspend fun likeSong(
        @Header("Authorization") accessToken: String,
        @Part("songId") id: RequestBody
    ): DataApi

    @POST("api/playlist")
    @Multipart
    suspend fun createPlayList(
        @Header("Authorization") accessToken: String,
        @Part("name") name: RequestBody,
        @Part("isPublic") isPublic: RequestBody
    ): DataApi
    @POST("api/playlist/addSong")
    @Multipart
    suspend fun addSongPlayList(
        @Header("Authorization") accessToken: String,
        @Part("playlistId") playlistId: RequestBody,
        @Part("listSongId[]") songId: RequestBody
    ): DataApi

    @DELETE("api/favourite/{id}")
    suspend fun unLikeSong(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): ResponseBody


    @Streaming
    @GET
    suspend fun downloadMusic(@Url url: String?): ResponseBody

    @Streaming
    @GET
    suspend fun downloadLyric(@Url url: String): ResponseBody

    @Streaming
    @GET
    suspend fun downloadImg(@Url url: String): ResponseBody


}
