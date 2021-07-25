package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.ActivityMainBinding
import com.baokiin.mymusic.ui.CategoryFragment
import com.baokiin.mymusic.ui.InfoFragment
import com.baokiin.mymusic.ui.home.HomeCallBack
import com.baokiin.mymusic.ui.home.HomeFragment
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.Utils.BITMAP
import com.baokiin.mymusic.utils.Utils.SONG
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.play_music.*
import kotlinx.android.synthetic.main.play_music.view.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        baseBinding.apply {
            lifecycleOwner = this@MainActivity
            viewmodel = viewModel
        }
        utilView()
        setupTablayout()
        clickView()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MediaService::class.java)
        stopService(intent)
    }


    private fun utilView() {
        val fragmentHome = HomeFragment()
        fragmentHome.clickCallBack(object : HomeCallBack {
            override fun clickItem(song: Song, bitmap: Bitmap) {
                playMusic.apply {
                    visibility = View.VISIBLE
                    image.load(bitmap)
                    txtNameMusic.text = song.name
                    txtArtists.text = song.artists_names
                }
                startMedia(song, bitmap)
            }

        })

        viewModel.adapter =
            ViewPageAdapter(mutableListOf(fragmentHome, CategoryFragment(), InfoFragment()), this)
    }

    private fun setupTablayout() {
        viewpager.isUserInputEnabled = false
        viewpager.adapter = viewModel.adapter
        TabLayoutMediator(
            tabLayout,
            viewpager
        ) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.setIcon(R.drawable.ic_home)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_trend)
                }

                2 -> {
                    tab.setIcon(R.drawable.ic_account)
                }
            }
        }.attach()
    }

    private fun clickView() {


    }

    private fun startMedia(song: Song, bitmap: Bitmap) {
        val intent = Intent(this, MediaService::class.java)
        intent.putExtra(SONG, song)
        intent.putExtra(BITMAP, bitmap)
        startForegroundService(intent)

    }
}