package com.baokiin.mymusic.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.sns.SNSLoginActivity
import com.baokiin.mymusic.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class LoginActivity : SNSLoginActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        init()

    }

    override fun onSNSUserResult() {
        EventBus.getDefault().post(EventBusModel.DataChange(Utils.STATUS_LOGIN_OK))
        finish()
    }

    //----------------------------
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