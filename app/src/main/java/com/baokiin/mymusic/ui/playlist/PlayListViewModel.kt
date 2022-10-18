package com.baokiin.mymusic.ui.playlist

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.PlayList
import com.baokiin.mymusic.data.respository.Repository
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    val liveData = MutableLiveData<DataApi>()
    val publicPlayListliveData = MutableLiveData<MutableList<PlayList>?>()
    val createPlayListliveData = MutableLiveData<DataApi?>()
    fun getData(context: Context, type: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetPing(context)) {
                liveData.postValue(
                    when (type) {
                        Utils.VPOP -> repo.getTopVpop()
                        Utils.KPOP -> repo.getTopKpop()
                        Utils.USUK -> repo.getTopAmerica()
                        else -> repo.getTopVpop()
                    }
                )
            } else
                Toast.makeText(
                    context,
                    "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    fun getPublicPlayList(context: Context, type: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetPing(context)) {
                publicPlayListliveData.postValue(
                    if (type == Utils.PUBLIC_PLAYLIST)
                        repo.getPublicPlayList().data
                    else if (type == Utils.PRIVATE_PLAYLIST)
                        repo.getPrivatePlayList().data
                    else repo.getPublicPlayList().data
                )
            } else
                Toast.makeText(
                    context,
                    "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    fun createPlayList(context: Context,name:String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utils.isInternetPing(context)) {
                createPlayListliveData.postValue(repo.createPlayList(AppData.g().token,name))
            } else
                Toast.makeText(
                    context,
                    "Thiết bị kết nối mạng bị gián đoạn, sẽ hiện thị theo offline!!",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }
}