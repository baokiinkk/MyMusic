package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import com.baokiin.mymusic.data.remote.api.ApiService
import com.baokiin.mymusic.data.remote.api.FindMusicService
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.sns.SharedPreferencesUtils
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val findMusicService: FindMusicService
) : Repository {
    override suspend fun getTrending(): DataApi =
        try {
            apiService.getTrending()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun login(): DataApi =
        try {
            apiService.login()
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun getTopAmerica(): DataApi =
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

    override suspend fun getSongsLiked(): DataApi =
        try {
            apiService.getSongsLiked(AppData.g().token)
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun search(id: String): DataFind =
        try {
            findMusicService.getSong(id)
        } catch (cause: HttpException) {
            DataFind(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun likeSong(token:String,idSong: String): DataApi =
        try {
            val request = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("songId",idSong)
                .build()
            apiService.likeSong("Bearer $token",request)
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun unLikeSong(token:String,idSong: String): DataApi =
        try {
            apiService.unLikeSong("Bearer $token",idSong)
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }



    //download file
    override suspend fun downloadMusic(url: String): ResponseBody = apiService.downloadMusic(url)
    override suspend fun downloadLyric(url: String): ResponseBody = apiService.downloadLyric(url)
    override suspend fun downloadImg(url: String): ResponseBody = apiService.downloadImg(url)
}

