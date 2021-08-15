package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.Times
import com.baokiin.mymusic.databinding.ActivityMainBinding
import com.baokiin.mymusic.ui.CategoryFragment
import com.baokiin.mymusic.ui.InfoFragment
import com.baokiin.mymusic.ui.home.HomeFragment
import com.baokiin.mymusic.ui.lyric.LyricFragment
import com.baokiin.mymusic.ui.music.MusicFragment
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //--------------------------------- variable --------------------------------------------------
    private val viewModel by viewModels<MainViewModel>()


    //---------------------------- override lifecycle----------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utilView()
        setUp()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MediaService::class.java)
        stopService(intent)
        EventBus.getDefault().unregister(this)
    }


    //-------------------------------------- recive data ------------------------------------------

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageSecond(second: Times) {
        viewModel.positonMedia.postValue(second.time)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(song: Song) {
        playMusic.visibility = View.VISIBLE
        val intent = Intent(this, MediaService::class.java)
        intent.putExtra(Utils.SONG, song)
        startForegroundService(intent)
        viewModel.getSongs(song)
    }

    //-------------------------------------- func ------------------------------------------
    private fun setUp() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        EventBus.getDefault().register(this)
        val baseBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        baseBinding.apply {
            lifecycleOwner = this@MainActivity
            viewmodel = viewModel
        }
    }

    private fun utilView() {
        viewModel.adapter =
            ViewPageAdapter(mutableListOf(HomeFragment(), CategoryFragment(), InfoFragment()), this)
        viewModel.adapterMusic =
            ViewPageAdapter(mutableListOf(MusicFragment(), LyricFragment()), this)
        viewModel.songs.observe(this, {
            it?.let {
                EventBus.getDefault().post(it)
            }
        })


    }


}