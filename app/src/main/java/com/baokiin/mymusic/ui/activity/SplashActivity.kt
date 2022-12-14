package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.baokiin.mymusic.R
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.sns.SharedPreferencesUtils
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@AndroidEntryPoint
class SplashActivity: AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<HomeViewModel>()
        super.onCreate(savedInstanceState)
        instanceGoogleSignIn()
        AppData.g().token = SharedPreferencesUtils.getTokenID(this)
        if(!AppData.g().token.isNullOrBlank() && Utils.isInternetPing(this) ){
            viewModel.checkUser(AppData.g().token,this)
        }else{
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
        viewModel.checkUser.observe(this){
            it?.let {
                when(it.status){
                    "DELETED"->{
                        Toast.makeText(this,"Tài khoản đã bị xóa!!!!",Toast.LENGTH_SHORT).show()
                        googleSignInClient.signOut()
                        viewModel.auth.signOut()
                    }
                    "EXPIRED"->{
                        Toast.makeText(this,"Tài khoản đã hết hạn!!!!",Toast.LENGTH_SHORT).show()
                        googleSignInClient.signOut()
                        viewModel.auth.signOut()
                    }
                }
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
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