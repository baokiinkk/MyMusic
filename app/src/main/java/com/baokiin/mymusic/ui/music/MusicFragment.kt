package com.baokiin.mymusic.ui.music


import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentMusicBinding
import com.baokiin.mymusic.service.DownloadMusicService
import com.baokiin.mymusic.ui.activity.MainViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.startServiceMusic
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class MusicFragment : BaseFragment<FragmentMusicBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_music
    }

    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreateViews() {
        baseBinding.viewmodel = viewModel
        baseBinding.btnDown.setOnClickListener {
            val url =
                "http://api.mp3.zing.vn/api/streaming/audio/${viewModel.mediaInfo.value?.song?.id}/320"
            viewModel.downloadSong(url)
            viewModel.mediaInfo.value?.let {
                it.song.thumbnail?.let { it1 ->
                    viewModel.downloadImg(
                        it1,
                        requireContext(),
                        it.song
                    )
                }
            }
            val intent = Intent(context, DownloadMusicService::class.java)
            intent.putExtra(Utils.SONG, viewModel.mediaInfo.value?.song)
            startServiceMusic(requireActivity(), intent)
        }
        viewModel.apply {
            mediaInfo.observe(viewLifecycleOwner, {
                it?.let {
                    it.song.lyric?.let { it1 -> getLyric(it1, requireContext(), it.song) }
                }

            })
            downloadMusic.observe(viewLifecycleOwner, { download ->
                download?.let { response ->
                    mediaInfo.value?.song?.let { song ->
                            downloadImg.value?.let {
                                song.lyric = lyricFile.value?.path?:"aaaaaaaaaaa"
                                song.thumbnail = "file://$it"
                                song.isDownload = true
                                EventBus.getDefault().post(EventBusModel.Reponse(response, song))
                            }


                    }

                }
            })
        }
    }
}