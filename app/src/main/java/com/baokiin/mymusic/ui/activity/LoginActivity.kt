package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.SongLike
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.sns.SNSLoginActivity
import com.baokiin.mymusic.sns.SharedPreferencesUtils
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.Utils
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
    private val viewModel by viewModels<HomeViewModel>()
    private var tmpToken:String? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        instanceGoogleSignIn()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        init()

    }

    override fun onSNSUserResult(token: String?) {
        tmpToken = token
        viewModel.checkUser(token,this)
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
        viewModel.checkUser.observe(this){
            it?.let {
                when(it.status){
                    "DELETED"->{
                        Toast.makeText(this,"Tài khoản đã bị xóa!!!!", Toast.LENGTH_SHORT).show()
                        googleSignInClient.signOut()
                        viewModel.auth.signOut()
                    }
                    "EXPIRED"->{
                        Toast.makeText(this,"Tài khoản đã hết hạn!!!!", Toast.LENGTH_SHORT).show()
                        googleSignInClient.signOut()
                        viewModel.auth.signOut()
                    }
                    else ->{
                        EventBus.getDefault().post(EventBusModel.DataChange(Utils.STATUS_LOGIN_OK))
                        SharedPreferencesUtils.setTokenID(this,tmpToken)
                        AppData.g().token = tmpToken
                    }
                }
                finish()
            }
        }

    }
    private fun instanceGoogleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}