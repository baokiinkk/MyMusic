package com.baokiin.mymusic.data.api

import com.baokiin.mymusic.data.model.LoginUser
import com.baokiin.mymusic.data.model.Users
import retrofit2.http.GET

interface ApiService {
    @GET("media/get-source?type=album&key=LmxnyZHadxumRncynTDnkmyZgFWcsGCCp")
    suspend fun login(): Users

}