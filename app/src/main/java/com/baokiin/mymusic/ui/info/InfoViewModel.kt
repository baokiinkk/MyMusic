package com.baokiin.mymusic.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baokiin.mymusic.data.respository.Repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel@Inject constructor(private val repo: Repository): ViewModel() {
    var auth = MutableLiveData(Firebase.auth)
    val textNull = "Đăng Nhập"
}