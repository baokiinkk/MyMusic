package com.baokiin.mymusic.ui.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import com.baokiin.mymusic.data.respository.RepositoryLocal
import com.baokiin.mymusic.utils.Utils.writeResponseBodyToDisk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.*

import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val database: RepositoryLocal
) : ViewModel() {
    var adapter: ViewPageAdapter? = null
    var adapterMusic: ViewPageAdapter? = null
    val songs: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val mediaInfo: MutableLiveData<MediaInfo?> = MutableLiveData(null)
    val lyricFile: MutableLiveData<File?> = MutableLiveData(null)
    val downloadMusic: MutableLiveData<ResponseBody?> = MutableLiveData(null)
    val downloadImg: MutableLiveData<String?> = MutableLiveData(null)
    val positonMedia: MutableLiveData<Int?> = MutableLiveData(null)
    val isScroll: MutableLiveData<Boolean?> = MutableLiveData(null)
    fun getSongs(id: Song) {
        viewModelScope.launch {
            val song = repo.getSongs(id.id).data?.items
            songs.postValue(song)
        }
    }

    fun downloadSong(url: String) {
        viewModelScope.launch {
            downloadMusic.postValue(repo.downloadMusic(url))
        }
    }

    fun downloadImg(url: String, context: Context, song: Song) {
        viewModelScope.launch {
            val img = repo.downloadImg(url.replace("w94", "w360"))
            val path = context.getExternalFilesDir(null)
                .toString() + File.separator.toString() + song.id + ".jpg"
            val isDownload = writeResponseBodyToDisk(img, path) {}
            if (isDownload)
                downloadImg.postValue(path)
        }
    }

    fun getLyric(url: String, context: Context, song: Song) {
        viewModelScope.launch {
            if (url.substring(0, 4) != "http") {
                lyricFile.postValue(File(url))
            } else {
                val response = repo.downloadLyric(url)
                val path = context.getExternalFilesDir(null)
                    .toString() + File.separator.toString() + song.id + ".lrc"
                val isDownload = writeResponseBodyToDisk(response, path) {}
                if (isDownload)
                    lyricFile.postValue(File(path))
            }
        }
    }


    // database
    fun addSong(song: Song) {
        viewModelScope.launch {
            database.insertSong(song)
        }
    }

}