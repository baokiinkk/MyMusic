package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.baokiin.mymusic.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@AndroidEntryPoint
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<HomeViewModel>()
        super.onCreate(savedInstanceState)
        try {
            val info = getPackageManager().getPackageInfo(
                getPackageName(),
                PackageManager.GET_SIGNATURES);
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch ( e: PackageManager.NameNotFoundException) {

        }
        catch ( e: NoSuchAlgorithmException) {

        }
        viewModel.getData(this)
        viewModel.getDataFromFirestore()
        viewModel.america.observe(this) {
            it?.let {
                // Start home activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }

    }
}