package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.api.ApiService
import com.baokiin.mymusic.data.model.LoginUser
import com.baokiin.mymusic.data.model.Users
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {
    override suspend fun login(): Users =
        try {
            apiService.login()
        } catch (cause: HttpException) {
            Users(message = cause.response()?.errorBody()?.string())
        }

}

