package com.baokiin.mymusic.ui.lyric

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.ui.activity.MainViewModel
import com.lauzy.freedom.library.LrcHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_lyric.view.*
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class LyricFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lyric, container, false)
        viewModel.mediaInfo.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.getLyric(it.song.lyric,requireContext())
            }

        })
        viewModel.lyricFile.observe(viewLifecycleOwner, {
            it?.let {
                val lrcs = LrcHelper.parseLrcFromFile(it)
                view.lyricView.setLrcData(lrcs)
            }
        })
        viewModel.positonMedia.observe(viewLifecycleOwner,{
            it?.let {
                view.lyricView.updateTime(it.toLong())
            }
        })
        view.lyricView.setOnPlayIndicatorLineListener { time, _ ->
            EventBus.getDefault().post(TimesLong(time))
        }
        return view
    }

}