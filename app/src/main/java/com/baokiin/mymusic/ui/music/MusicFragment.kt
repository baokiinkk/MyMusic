package com.baokiin.mymusic.ui.music


import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentMusicBinding
import com.baokiin.mymusic.service.DownloadMusicService
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.ui.activity.LoginActivity
import com.baokiin.mymusic.ui.activity.MainViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.startServiceMusic
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


@AndroidEntryPoint
class MusicFragment : BaseFragment<FragmentMusicBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_music
    }

    private val viewModel by activityViewModels<MainViewModel>()
    private var isDownloaded: Boolean? = null
    private var isLiked: Boolean? = null
    private var auth = Firebase.auth
    override fun onCreateViews() {

        setUp()
        onViewCLick()

    }

    private fun setUp() {

        baseBinding.viewmodel = viewModel
        viewModel.apply {
            mediaInfo.observe(viewLifecycleOwner) {
                it?.let {
                    baseBinding.btnLike.visibility = if (it.song.link?.substring(
                            1,
                            8
                        ) == "storage"
                    ) View.INVISIBLE else View.VISIBLE
                    it.song.lyric?.let { it1 -> getLyric(it1, requireContext(), it.song) }
                }
                buttonType(baseBinding.btnLike, baseBinding.btnLike.background, it?.song?.isLiked)

            }
            songFromDatabase.observe(viewLifecycleOwner) {
                isDownloaded = it
                buttonType(baseBinding.btnDown, baseBinding.btnDown.background, it)
            }
            downloading.observe(viewLifecycleOwner) {
                it?.let {
                    isDownloaded = it
                    buttonType(baseBinding.btnDown, baseBinding.btnDown.background, it)
                }
            }
            downloadMusic.observe(viewLifecycleOwner) { download ->
                download?.let { response ->
                    mediaInfo.value?.song?.let { song ->
                        downloadImg.value?.let {
                            EventBus.getDefault().post(
                                EventBusModel.Response(
                                    response,
                                    song,
                                    lyricFile.value?.path ?: "aaaaaaaaaaa",
                                    "file://$it"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun buttonType(button: Button, drawable: Drawable, check: Boolean?) {
        val buttonDownload = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(
            buttonDownload,
            requireContext().getColor(if (check == true) R.color.blue_color else R.color.white)
        )
        button.background = buttonDownload
    }


    private fun onViewCLick() {
        baseBinding.btnDown.setOnClickListener {
            if (isDownloaded == true)
                Toast.makeText(context, "Bạn đã tải bài hát này.", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(context, DownloadMusicService::class.java)
                startServiceMusic(requireActivity(), intent)
                val url =
                    "https://api.mp3.zing.vn/api/streaming/audio/${viewModel.mediaInfo.value?.song?.songId}/320"
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
            }
        }
        baseBinding.btnLike.setOnClickListener {
            if (auth.currentUser == null)
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            else {
                val song = viewModel.mediaInfo.value?.song
                song?.let {
                    if (isLiked != true) {
                        isLiked = true
                        buttonType(baseBinding.btnLike, baseBinding.btnLike.background, true)
                        it.isLiked = isLiked
                        viewModel.addSongLike(AppData.g().token?:"",it)
                    } else {
                        isLiked = false
                        buttonType(baseBinding.btnLike, baseBinding.btnLike.background, false)
                        it.isLiked = isLiked
                        viewModel.deleteSongLike(AppData.g().token?:"",it)
                    }


                }

            }
        }
    }
}