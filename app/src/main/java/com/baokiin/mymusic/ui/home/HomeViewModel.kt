package com.baokiin.mymusic.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    val trending:MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val kpop:MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val vpop:MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val america:MutableLiveData<MutableList<Song>?> = MutableLiveData(null)
    val amedicaName = "Top 100 USUK"
    val kpopName = "Top 100 Kpop"
    val vpopName = "Top 100 VPop"
    val imageTitle = R.drawable.title_image
    fun getData(){
        viewModelScope.launch(Dispatchers.IO){
            val dataTrend = repo.getTrending().data?.song?.subList(0, 6)
            dataTrend?.add(0, dataTrend[5])
            dataTrend?.add(dataTrend[1])
            trending.postValue(dataTrend)
            kpop.postValue(repo.getTopKpop().data?.items?.subList(0, 6))
            vpop.postValue(repo.getTopVpop().data?.items?.subList(0, 6))
            america.postValue(repo.getTopAmerica().data?.items?.subList(0, 6))
        }
    }
}