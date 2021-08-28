package com.baokiin.mymusic.data.remote.api

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @GET("chart-realtime?songId=0&videoId=0&albumId=0&chart=song&time=-1")
    suspend fun getTrending(): DataApi

    @GET("media/get-source?type=album&key=LmJHtkGNBJRmcWiymtDGZGyZhDQxsZQSa")
    suspend fun getTopAmerica(): DataApi

    @GET("media/get-source?type=album&key=LmxnyZHadxumRncynTDnkmyZgFWcsGCCp")
    suspend fun getTopKpop(): DataApi

    @GET("media/get-source?type=album&key=kncHyLmNdcRmczgtHyDmkGTLhvpcsHALs")
    suspend fun getTopVpop(): DataApi

    @GET("recommend?type=audio")
    suspend fun getSongs(@Query("id") id:String):DataApi


    @Streaming
    @GET
    suspend fun downloadMusic(@Url url:String):ResponseBody

    @Streaming
    @GET
    suspend fun downloadLyric(@Url url:String):ResponseBody


}
interface FindMusicService {
    @GET("complete?type=artist,song,key,code&num=50")
    suspend fun getSong(@Query("query") id:String):DataFind

}