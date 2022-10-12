package com.baokiin.mymusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.Data
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.databinding.ActivityMainBinding
import com.baokiin.mymusic.service.DownloadMusicService
import com.baokiin.mymusic.service.MediaService
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.sns.SharedPreferencesUtils
import com.baokiin.mymusic.ui.lyric.LyricFragment
import com.baokiin.mymusic.ui.music.MusicFragment
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.startServiceMusic
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //--------------------------------- variable --------------------------------------------------
    private val viewModel by viewModels<MainViewModel>()
    private var indexSong: Int? = null


    //---------------------------- override lifecycle----------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        setFullScreen(ActivityCompat.getColor(this, R.color.transparent))
        super.onCreate(savedInstanceState)
        utilView()
        setUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MediaService::class.java)
        stopService(intent)

        val intent2 = Intent(this, DownloadMusicService::class.java)
        stopService(intent2)
        EventBus.getDefault().unregister(this)
    }


    //-------------------------------------- recive data ------------------------------------------

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageSecond(mp3: DownloadMp3) {
        viewModel.addSongDownload(mp3.song)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageSecond(second: Times) {
        viewModel.positonMedia.postValue(second.time)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(song: SongSingle) {
        try {
            playMusic.visibility = View.VISIBLE
            val intent = Intent(this, MediaService::class.java)
            val data = Data(song = song.isList)
            intent.putExtra(Utils.SONG, Gson().toJson(data))
            startServiceMusic(this, intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Xin thử lại!", Toast.LENGTH_SHORT).show()
        }


    }

    //-------------------------------------- func ------------------------------------------
    private fun setUp() {
        AppData.g().idToken = SharedPreferencesUtils.getTokenID(this)
        EventBus.getDefault().register(this)
        val baseBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        baseBinding.apply {
            lifecycleOwner = this@MainActivity
            viewmodel = viewModel
        }

    }

    private fun utilView() {
        viewModel.adapterMusic =
            ViewPageAdapter(mutableListOf(MusicFragment(), LyricFragment()), this)
    }

    fun setFullScreen(colorStatusBar: Int) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = colorStatusBar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}