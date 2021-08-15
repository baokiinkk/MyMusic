package com.baokiin.mymusic.ui.music


import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.TimesLong
import com.baokiin.mymusic.databinding.FragmentMusicBinding
import com.baokiin.mymusic.ui.activity.MainViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.google.android.material.slider.Slider
import org.greenrobot.eventbus.EventBus

class MusicFragment : BaseFragment<FragmentMusicBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_music
    }
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreateViews() {
        baseBinding.viewmodel = viewModel
        baseBinding.seekBarMedia.apply {
            setLabelFormatter {
                val longSecond: Long = (it/1000).toLong()
                String.format("%02d:%02d",(longSecond % 3600) / 60, longSecond % 60)
            }
            addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}

                override fun onStopTrackingTouch(slider: Slider) {
                    EventBus.getDefault().post(TimesLong(slider.value.toLong()))
                }
            })
            viewModel.mediaInfo.observe(viewLifecycleOwner, {
                it?.let {
                    valueTo = it.mediaPlayer.duration.toFloat()
                }
            })
            viewModel.positonMedia.observe(viewLifecycleOwner,{
                it?.let {
                    value = it.toFloat()
                }
            })
        }

    }
}