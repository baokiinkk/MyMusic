package com.baokiin.mymusic.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    val trending:MutableLiveData<DataApi?> = MutableLiveData(null)
    val kpop:MutableLiveData<DataApi?> = MutableLiveData(null)
    val vpop:MutableLiveData<DataApi?> = MutableLiveData(null)
    val america:MutableLiveData<DataApi?> = MutableLiveData(null)
    val amedicaName = "TOP 100 USUK"
    val kpopName = "TOP 100 KPOP"
    val vpopName = "TOP 100 VPOP"
    val imageMain = R.drawable.background_main
    val imageTrend = R.drawable.bg_chart_music
    val imageMy = R.drawable.bg_my_like_music

    fun getData(){
        viewModelScope.launch(Dispatchers.IO){
            val dataTrend = repo.getTrending()
            trending.postValue(dataTrend)
            kpop.postValue(repo.getTopKpop())
            vpop.postValue(repo.getTopVpop())
            america.postValue(repo.getTopAmerica())
        }
    }
}