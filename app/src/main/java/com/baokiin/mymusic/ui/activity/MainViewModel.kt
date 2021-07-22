package com.baokiin.mymusic.ui.activity

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.transition.Visibility
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    var adapter: ViewPageAdapter? =  null
    var check = View.GONE
}