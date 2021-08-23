package com.baokiin.mymusic.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    var adapter: ViewPageAdapter? = null
    var adapterMusic: ViewPageAdapter? = null
    val songs: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val mediaInfo: MutableLiveData<MediaInfo?> = MutableLiveData(null)
    val lyricFile: MutableLiveData<File?> = MutableLiveData(null)
    val positonMedia: MutableLiveData<Int?> = MutableLiveData(null)
    val isScroll: MutableLiveData<Boolean?> = MutableLiveData(null)
    fun getSongs(id: Song) {
        viewModelScope.launch {
            val song = repo.getSongs(id.id).data?.items
            songs.postValue(song)
        }
    }

    fun getLyric(url: String?) {
        GlobalScope.launch {
            url?.let {
                lyricFile.postValue(getFileByUrl(it))
            }

        }
    }

    private fun getFileByUrl(fileUrl: String): File? {
        val outStream = ByteArrayOutputStream()
        var stream: BufferedOutputStream? = null
        var inputStream: InputStream? = null
        var file: File? = null
        try {
            val imageUrl = URL(fileUrl)
            val conn: HttpURLConnection = imageUrl.openConnection() as HttpURLConnection
            conn.setRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
            )
            inputStream = conn.inputStream
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                outStream.write(buffer, 0, len)
            }
            file = File.createTempFile(
                "file",
                fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length)
            )
            val fileOutputStream = FileOutputStream(file)
            stream = BufferedOutputStream(fileOutputStream)
            stream.write(outStream.toByteArray())
        } catch (e: Exception) {
        } finally {
            try {
                inputStream?.close()
                stream?.close()
                outStream.close()
            } catch (e: Exception) {
            }
        }
        return file
    }
}