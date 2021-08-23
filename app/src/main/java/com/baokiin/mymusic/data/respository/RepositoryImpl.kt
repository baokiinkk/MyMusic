package com.baokiin.mymusic.data.respository

import android.util.Log
import com.baokiin.mymusic.data.api.ApiService
import com.baokiin.mymusic.data.api.FindMusicService
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: ApiService,private val findMusicService: FindMusicService) : Repository {
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

    override suspend fun getSongs(id: String): DataApi =
        try {
            apiService.getSongs(id)
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun search(id: String): DataFind =
        try {
            Log.d("quocbao",id)
            findMusicService.getSong(id)
        } catch (cause: HttpException) {
            DataFind(message = "lấy thông tin lỗi!!!")
        }

}

