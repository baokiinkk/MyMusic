package com.baokiin.mymusic.ui.music


import android.content.Intent
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentMusicBinding
import com.baokiin.mymusic.service.DownloadMusicService
import com.baokiin.mymusic.service.MediaService
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
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${viewModel.mediaInfo.value?.song?.id}/320"
            viewModel.downloadSong(url)
            val intent = Intent(context, DownloadMusicService::class.java)
            intent.putExtra(Utils.SONG, viewModel.mediaInfo.value?.song)
            startServiceMusic(requireActivity(),intent)
        }
        viewModel.downloadMusic.observe(viewLifecycleOwner,{
            it?.let {
                viewModel.mediaInfo.value?.song?.let {song->
                    EventBus.getDefault().post(EventBusModel.Reponse(it,song))
                }

            }
        })
    }
}