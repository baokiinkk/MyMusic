package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.DataFind
import com.baokiin.mymusic.data.model.DataPlayListApi
import com.baokiin.mymusic.data.remote.api.ApiService
import com.baokiin.mymusic.data.remote.api.FindMusicService
import com.baokiin.mymusic.sns.AppData
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
            apiService.getSongsLiked("Bearer ${AppData.g().token?:""}")
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun search(id: String): DataFind =
        try {
            findMusicService.getSong(id)
        } catch (cause: HttpException) {
            DataFind(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun likeSong(token: String, idSong: String): DataApi =
        try {
            val request =  RequestBody.create(
                MediaType.parse("text/plain"),
                idSong
            )
            apiService.likeSong("Bearer $token", request)
        } catch (cause: HttpException) {
            DataApi(message = "lấy thông tin lỗi!!!")
        }
    override suspend fun createPlayList(token: String?, name: String): DataApi =
            try {
                val nameRequest =  RequestBody.create(
                    MediaType.parse("text/plain"),
                    name
                )
                val isPublicRequest =  RequestBody.create(
                    MediaType.parse("text/plain"),
                    "false"
                )
                apiService.createPlayList("Bearer $token", nameRequest,isPublicRequest)
            } catch (cause: HttpException) {
                DataApi(message = "lấy thông tin lỗi!!!")
            }
    override suspend fun addSongPlayList(playlistId: String,songId:String): DataApi =
            try {
                val playlistIdRequest =  RequestBody.create(
                    MediaType.parse("text/plain"),
                    playlistId
                )
                val songIdRequest =  RequestBody.create(
                    MediaType.parse("text/plain"),
                    songId
                )
                apiService.addSongPlayList("Bearer ${AppData.g().token}", playlistIdRequest,songIdRequest)
            } catch (cause: HttpException) {
                DataApi(message = "Bài hát đã được thêm vào danh sách!")
            }

    override suspend fun getPublicPlayList(): DataPlayListApi =
        try {
            apiService.getPublicPlayList()
        } catch (cause: HttpException) {
            DataPlayListApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun getPrivatePlayList(): DataPlayListApi =
        try {
            apiService.getMyPlayList("Bearer ${AppData.g().token}")
        } catch (cause: HttpException) {
            DataPlayListApi(message = "lấy thông tin lỗi!!!")
        }

    override suspend fun unLikeSong(token: String, idSong: String): ResponseBody =
            apiService.unLikeSong("Bearer $token", idSong)

    //download file
    override suspend fun downloadMusic(url: String?): ResponseBody = apiService.downloadMusic(url)
    override suspend fun downloadLyric(url: String): ResponseBody = apiService.downloadLyric(url)
    override suspend fun downloadImg(url: String): ResponseBody = apiService.downloadImg(url)
}

