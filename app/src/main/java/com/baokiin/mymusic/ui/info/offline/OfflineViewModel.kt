package com.baokiin.mymusic.ui.info.offline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.RepositoryLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineViewModel @Inject constructor(private val database: RepositoryLocal) : ViewModel() {
    val songs: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    fun getSongs() {
        viewModelScope.launch {
            val song = database.getDataSong()
            songs.postValue(song)
        }
    }
}
