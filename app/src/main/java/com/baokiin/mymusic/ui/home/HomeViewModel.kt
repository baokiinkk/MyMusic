package com.baokiin.mymusic.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.data.model.Data
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    val songs:MutableLiveData<Data?> = MutableLiveData(null)
    fun getTrending(){
        viewModelScope.launch(Dispatchers.IO){
            songs.postValue(repo.getTrending().data)
        }
    }
}