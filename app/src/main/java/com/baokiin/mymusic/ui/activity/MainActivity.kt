package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.MediaInfo
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.ActivityMainBinding
import com.baokiin.mymusic.ui.CategoryFragment
import com.baokiin.mymusic.ui.InfoFragment
import com.baokiin.mymusic.ui.home.HomeFragment
import com.baokiin.mymusic.ui.music.MusicFragment
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.ACTION_NEXT
import com.baokiin.mymusic.utils.Utils.ACTION_PLAY
import com.baokiin.mymusic.utils.Utils.ACTION_PREV
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.play_music.*
import kotlinx.android.synthetic.main.play_music.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
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
        EventBus.getDefault().unregister(this)
    }


    //-------------------------------------- recive ---------------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(mediaInfo: MediaInfo) {
        playMusic.btnPlayPauseMainActivity.setBackgroundResource(if (mediaInfo.mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
        viewModel.mediaInfo.postValue(mediaInfo)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(song: Song) {
        playMusic.visibility = View.VISIBLE
        val intent = Intent(this, MediaService::class.java)
        intent.putExtra(Utils.SONG, song)
        startForegroundService(intent)
        viewModel.getSongs(song)
    }
    private fun setUp(){
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
        viewModel.adapterMusic = ViewPageAdapter(mutableListOf(MusicFragment()), this)
        viewModel.songs.observe(this, {
            it?.let {
                EventBus.getDefault().post(it)
            }
        })
    }

    private fun setupTablayout() {
        viewpager.isUserInputEnabled = false
        viewpager.adapter = viewModel.adapter
        playMusic.viewpagerMusic.adapter = viewModel.adapterMusic
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
        playMusic.apply {
            btnPlay.setOnClickListener { sendActionService(ACTION_PLAY) }
            btnNext.setOnClickListener { sendActionService(ACTION_NEXT) }
            btnPrev.setOnClickListener { sendActionService(ACTION_PREV) }
        }
    }

    private fun sendActionService(action: Int) {
        val intentService = Intent(this, MediaService::class.java)
        intentService.putExtra(Utils.ACTION, action)
        startForegroundService(intentService)
    }

}