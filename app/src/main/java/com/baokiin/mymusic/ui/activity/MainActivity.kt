package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.databinding.ActivityMainBinding
import com.baokiin.mymusic.ui.InfoFragment
import com.baokiin.mymusic.ui.home.HomeFragment
import com.baokiin.mymusic.ui.lyric.LyricFragment
import com.baokiin.mymusic.ui.music.MusicFragment
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.ui.trend.TrendingFragment
import com.baokiin.mymusic.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.play_music.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        if (containerFramgnet.isVisible) {
            containerFramgnet.visibility = View.GONE
        } else
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
    fun onMessageSecond(showFrament: ShowFrament) {
        containerFramgnet.visibility = if (showFrament.boolean) View.VISIBLE else View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(song: SongSingle) {
        try {
            playMusic.visibility = View.VISIBLE
            val intent = Intent(this, MediaService::class.java)
            intent.putExtra(Utils.SONG, song.song)
            startForegroundService(intent)
            if (song.isList == null) {
                viewModel.getSongs(song.song)
            } else {
                GlobalScope.launch {
                    delay(1000)
                    viewModel.songs.postValue(song.isList)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Xin thử lại!", Toast.LENGTH_SHORT).show()
        }


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
            ViewPageAdapter(mutableListOf(HomeFragment(), TrendingFragment(), InfoFragment()), this)
        viewModel.adapterMusic =
            ViewPageAdapter(mutableListOf(MusicFragment(), LyricFragment()), this)
        viewModel.songs.observe(this, {
            it?.let {
                EventBus.getDefault().post(it)
            }
        })


    }


}