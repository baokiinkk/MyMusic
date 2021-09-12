package com.baokiin.mymusic.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class SearchViewModel@Inject constructor(private val repo: Repository): ViewModel() {
    val trending: MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    fun getData(text:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                trending.postValue(repo.search(text).data?.get(0)?.song)
            }
            catch (e:Exception){}
        }
}}