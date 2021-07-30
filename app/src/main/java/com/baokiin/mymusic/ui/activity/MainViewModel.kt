package com.baokiin.mymusic.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.MediaInfo
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    var adapter: ViewPageAdapter? =  null
    var adapterMusic: ViewPageAdapter? =  null
    val songs: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val mediaInfo:MutableLiveData<MediaInfo?> = MutableLiveData(null)
    fun getSongs(id:Song){
        viewModelScope.launch {
            val song = repo.getSongs(id.id).data?.items
            songs.postValue(song)
        }
    }
}