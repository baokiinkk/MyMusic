package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<HomeViewModel>()
        super.onCreate(savedInstanceState)

        viewModel.getData(this)
        viewModel.getDataFromFirestore()
        viewModel.america.observe(this,{
            it?.let {
                // Start home activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        })

    }
}