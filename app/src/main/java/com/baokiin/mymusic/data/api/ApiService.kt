package com.baokiin.mymusic.data.api

import com.baokiin.mymusic.data.model.DataApi
import retrofit2.http.GET

interface ApiService {
    @GET("chart-realtime?songId=0&videoId=0&albumId=0&chart=song&time=-1")
    suspend fun getTrending(): DataApi

}