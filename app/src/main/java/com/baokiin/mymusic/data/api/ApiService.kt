package com.baokiin.mymusic.data.api

import com.baokiin.mymusic.data.model.DataApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("chart-realtime?songId=0&videoId=0&albumId=0&chart=song&time=-1")
    suspend fun getTrending(): DataApi

    @GET("media/get-source?type=album&key=LmJHtkGNBJRmcWiymtDGZGyZhDQxsZQSa")
    suspend fun getTopAmerica(): DataApi

    @GET("media/get-source?type=album&key=LmxnyZHadxumRncynTDnkmyZgFWcsGCCp")
    suspend fun getTopKpop(): DataApi

    @GET("media/get-source?type=album&key=kncHyLmNdcRmczgtHyDmkGTLhvpcsHALs")
    suspend fun getTopVpop(): DataApi

    @GET("https://mp3.zing.vn/xhr/recommend?type=audio")
    suspend fun getSongs(@Query("id") id:String):DataApi
}