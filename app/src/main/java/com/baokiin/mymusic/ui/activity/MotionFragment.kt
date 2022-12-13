package com.baokiin.mymusic.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.EventBusModel.MediaInfo
import com.baokiin.mymusic.data.model.EventBusModel.TimesLong
import com.baokiin.mymusic.databinding.PlayMusicBinding
import com.baokiin.mymusic.service.MediaService
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.diaLogBottom
import com.baokiin.mymusic.utils.Utils.startServiceMusic
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MotionFragment : BaseFragment<PlayMusicBinding>() {
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
        viewModel.getIdSong(mediaInfo.song)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBackEvent(onBack: EventBusModel.OnBackEvent) {
        if (baseBinding.motionLayout.progress == 1f) {
            EventBus.getDefault().post(EventBusModel.OnBackPlayMusicEvent(true))
        } else
            Utils.setProgresMotion(baseBinding.motionLayout, 1f)
    }

    //-------------------------------------- func -----------------------------------------------
    private fun setUp() {
        EventBus.getDefault().register(this)
        baseBinding.viewmodel = viewModel
        baseBinding.viewpagerMusic.adapter = viewModel.adapterMusic

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun click() {

        baseBinding.apply {
            btnPlay.setOnClickListener { sendActionService(Utils.ACTION_PLAY) }
            btnNext.setOnClickListener { sendActionService(Utils.ACTION_NEXT) }
            btnPrev.setOnClickListener { sendActionService(Utils.ACTION_PREV) }
            btnShuffleMusic.setOnClickListener {
                viewModel.getPlayList(requireContext())
            }
            btncloseMusic.setOnTouchListener { _, event ->
                val process = motionLayout.progress

                val heightDevice = Resources.getSystem().displayMetrics.heightPixels
                var progress = event.rawY / heightDevice

                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        progress = if (progress >= 0.6) 1f else 0f
                    }
                }
                Utils.setProgresMotion(motionLayout, progress)

                true
            }

            seekBarMedia.apply {

                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        baseBinding.txtPositisionMusic.text = timeToText(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        EventBus.getDefault()
                            .post(seekBar?.progress?.toLong()?.let { TimesLong(it) })
                    }

                })

                viewModel.mediaInfo.observe(viewLifecycleOwner) {
                    it?.let {
                        val duration = it.mediaPlayer.duration
                        this.max = duration
                        baseBinding.txtDucation.text = timeToText(duration)
                    }
                }
                viewModel.positonMedia.observe(viewLifecycleOwner) {
                    it?.let {
                        this.progress = it
                    }
                }
                viewModel.playListliveData.observe(viewLifecycleOwner) {
                    it?.let {
                        diaLogBottom(requireActivity(), it) { playList, i ->
                            viewModel.addSongPlayList(requireContext(), playList)
                        }.show()
                    }
                }
                viewModel.addSongliveData.observe(viewLifecycleOwner) {
                    it?.let {
                        val mess =
                            if (it.message.isNullOrBlank()) "Bạn đã thêm thành công" else it.message
                        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun timeToText(duration: Int): String {
        val longSecond: Long = (duration / 1000).toLong()
        return String.format("%02d:%02d", (longSecond % 3600) / 60, longSecond % 60)
    }

    private fun sendActionService(action: Int) {
        val intentService = Intent(context, MediaService::class.java)
        intentService.putExtra(Utils.ACTION, action)
        startServiceMusic(requireActivity(), intentService)
    }
}
