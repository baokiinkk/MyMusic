package com.baokiin.mymusic.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.sns.SNSLoginActivity
import com.baokiin.mymusic.sns.SharedPreferencesUtils
import com.baokiin.mymusic.utils.Utils
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class LoginActivity : SNSLoginActivity() {
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        init()

    }

    override fun onSNSUserResult(token: String?) {
        EventBus.getDefault().post(EventBusModel.DataChange(Utils.STATUS_LOGIN_OK))
        SharedPreferencesUtils.setTokenID(this,token)
        AppData.g().token = token
        finish()

    }

    //---------\
    // -------------------
    private fun init() {
        //Instance firebase auth
        auth = Firebase.auth

        button_loginGg.setOnClickListener {
            signInGoogle()
        }
        button_loginFb.setOnClickListener {
            signInFb()
        }
        btnClose.setOnClickListener {
            finish()
        }

    }

}