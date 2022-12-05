package com.baokiin.mymusic.ui.home

import android.content.Context
import android.util.JsonToken
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.data.model.UserStatus
import com.baokiin.mymusic.data.respository.Repository
import com.baokiin.mymusic.data.respository.RepositoryLocal
import com.baokiin.mymusic.utils.Utils.isInternetPing
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: Repository,private val database:RepositoryLocal) : ViewModel() {
    var auth = Firebase.auth
    val trending: MutableLiveData<DataApi?> = MutableLiveData(null)
    val kpop: MutableLiveData<DataApi?> = MutableLiveData(null)
    val vpop: MutableLiveData<DataApi?> = MutableLiveData(null)
    val america: MutableLiveData<DataApi?> = MutableLiveData(null)
    val checkUser: MutableLiveData<UserStatus?> = MutableLiveData(null)
    val amedicaName = "Bảng xếp hạng nhạc Âu-Mỹ"
    val kpopName = "Bảng xếp hạng nhạc Hàn Quốc"
    val vpopName = "Bảng xếp hạng nhạc Việt"
    val imageMain = R.drawable.background_main

    fun checkUser(token: String?,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetPing(context)) {
                checkUser.postValue(repo.checkUser(token?:""))
            } else
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        context,
                        "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }
    fun getData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetPing(context)) {
                repo.getTrending().let {
                    if (it.message == null)
                        trending.postValue(it)
                }
                repo.getTopKpop().let {
                    if (it.message == null)
                        kpop.postValue(it)
                }
                repo.getTopVpop().let {
                    if (it.message == null)
                        vpop.postValue(it)
                }
                repo.getTopAmerica().let {
                    if (it.message == null)
                        america.postValue(it)
                }
            } else
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