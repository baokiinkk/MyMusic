package com.baokiin.mymusic.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.view.MotionEvent
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.MediaInfo
import com.baokiin.mymusic.databinding.PlayMusicBinding
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MotionFragment : BaseFragment<PlayMusicBinding>(){
    override fun getLayoutRes(): Int {
        return R.layout.play_music
    }
    //-------------------------------------- create variable ------------------------------------
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreateViews() {
        setUp()
        click()
    }
    //-------------------------------------- recive ---------------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(mediaInfo: MediaInfo) {
        baseBinding.btnPlayPauseMainActivity.setBackgroundResource(if (mediaInfo.mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
        viewModel.mediaInfo.postValue(mediaInfo)
    }
    //-------------------------------------- func -----------------------------------------------
    private fun setUp(){
        EventBus.getDefault().register(this)
        baseBinding.viewmodel = viewModel
        baseBinding.viewpagerMusic.adapter = viewModel.adapterMusic

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun click(){

        baseBinding.apply {
            btnPlay.setOnClickListener { sendActionService(Utils.ACTION_PLAY) }
            btnNext.setOnClickListener { sendActionService(Utils.ACTION_NEXT) }
            btnPrev.setOnClickListener { sendActionService(Utils.ACTION_PREV) }
            btncloseMusic.setOnTouchListener { _, event ->
                val heightDevice = Resources.getSystem().displayMetrics.heightPixels
                var progress = event.rawY/heightDevice
                when(event.action){
                    MotionEvent.ACTION_UP->{
                        progress = if(progress>=0.6) 1f else 0f
                    }
                }
                Utils.setProgresMotion(motionLayout,progress)
                true
            }
        }
    }
    private fun sendActionService(action: Int) {
        val intentService = Intent(context, MediaService::class.java)
        intentService.putExtra(Utils.ACTION, action)
        requireActivity().startForegroundService(intentService)
    }
}