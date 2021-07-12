package com.baokiin.mymusic.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.LoginUser
import com.baokiin.mymusic.data.model.Users
import com.baokiin.mymusic.data.respository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    val img = R.drawable.brlogin
    var email = ""
    var password = ""
    var user:MutableLiveData<Users?> = MutableLiveData(null)
    fun login(){

        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            user.postValue(repo.login())
        }
    }
}