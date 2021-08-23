package com.baokiin.mymusic.data.api

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Named

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


}
interface FindMusicService {
    @GET("complete?type=artist,song,key,code&num=50")
    suspend fun getSong(@Query("query") id:String):DataFind

}