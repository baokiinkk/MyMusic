package com.baokiin.mymusic.ui.info

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.data.respository.Repository
import com.baokiin.mymusic.data.respository.RepositoryLocal
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.STATUS_GETDATA
import com.baokiin.mymusic.utils.Utils.STATUS_UPDATA
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val repo: Repository,
    private val database: RepositoryLocal
) : ViewModel() {
    var auth = MutableLiveData(Firebase.auth)
    val textNull = "Đăng Nhập"
    val songIsDownloaded: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val songIsLiked: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val db = Firebase.firestore
    fun getSongsIsDownloaded() {
        viewModelScope.launch {
            val song = database.getSongDownloaded()
            songIsDownloaded.postValue(song)
        }
    }


    fun upData() {
        viewModelScope.launch {

            auth.value?.currentUser?.let { user ->
                val firebase = db.collection(user.uid)
                firebase.get()
                    .addOnSuccessListener { doc ->
                        doc.documents.forEach {
                            firebase.document(it.id).delete()
                        }
                    }
                val data = database.getSongLiked()
                data.forEach {
                    firebase.document(it.id).set(it)
                }
                auth.value?.signOut()
                database.deleteAllSongLike()
                EventBus.getDefault().post(EventBusModel.DataChange(STATUS_UPDATA))
            }
        }
    }

    fun getDataFromFirestore() {
        auth.value?.currentUser?.let {
            db.collection(it.uid).get()
                .addOnSuccessListener { doc ->
                    viewModelScope.launch(Dispatchers.IO) {
                        doc.documents.forEach {
                            val gson = Gson()
                            val jsonElement = gson.toJsonTree(it.data)
                            val data = gson.fromJson(jsonElement, SongLike::class.java)
                            database.insertSongLike(data)
                        }
                        EventBus.getDefault().post(EventBusModel.DataChange(STATUS_GETDATA))
                    }

                }
        }
    }

    fun deleteSongLike(context:Context,token: String, song: Song) {
        viewModelScope.launch {
            if (Utils.isInternetPing(context)) {
                repo.unLikeSong(token, song.songId)
                EventBus.getDefault().post(EventBusModel.LoadLocal(true))
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        context,
                        "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    fun getSongsLiked(context: Context) {
        viewModelScope.launch {
            try {
                val song = repo.getSongsLiked().data
                songIsLiked.postValue(song)
            }
            catch (e:Exception){
                Toast.makeText(
                    context,
                    "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun updateDataSong(songLike: MutableList<SongLike>, isOnline: Boolean = true) {
        viewModelScope.launch {
            if (isOnline) {
                database.deleteAllSongLike()
                songLike.forEach {
                    database.insertSongLike(it)
                }
            } else {
                database.deleteAllSong()
                songLike.forEach {
                    database.insertSong(it.toSong())
                }
            }
        }
    }
}