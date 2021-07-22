package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.api.ApiService
import com.baokiin.mymusic.data.model.DataApi
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService) : Repository {
    override suspend fun getTrending(): DataApi  =
        try {
            apiService.getTrending()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun getTopAmerica(): DataApi  =
        try {
            apiService.getTopAmerica()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun getTopKpop(): DataApi =
        try {
            apiService.getTopKpop()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun getTopVpop(): DataApi =
        try {
            apiService.getTopVpop()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

}

