package com.baokiin.mymusic.ui.info.online

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemSongLikeAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentSongLocalBinding
import com.baokiin.mymusic.sns.AppData
import com.baokiin.mymusic.ui.info.InfoViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OnlineFragment : BaseFragment<FragmentSongLocalBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_song_local
    }

    private lateinit var adapterItem: ItemSongLikeAdapter
    private val viewModel by activityViewModels<InfoViewModel>()
    override fun onCreateViews() {
        setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSecond(status: EventBusModel.LoadLocal) {
        viewModel.getSongsLiked()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangeData(status: EventBusModel.DataChange) {
        viewModel.getSongsLiked()
    }

    private fun setup() {
        EventBus.getDefault().register(this)
        adapterItem = ItemSongLikeAdapter { song, index ->
            EventBus.getDefault().post(
                EventBusModel.SongSingle(
                    song.toSong(),
                    adapterItem.currentList.map { it.toSong() }.toMutableList(),
                    index
                )
            )
        }
        baseBinding.apply {
            adapter = adapterItem

            val itemTouchHelper = ItemTouchHelper(
                Utils.callBackSwipe(
                    recycleviewLocal,
                    adapterItem
                ) {list,song->
                    if(song != null)
                        viewModel.deleteSongLike(AppData.g().token?:"",song.toSong())
                }
            )
            itemTouchHelper.attachToRecyclerView(recycleviewLocal)
        }
        viewModel.songIsLiked.observe(viewLifecycleOwner) {
            it?.let {
                if(it.isNotEmpty())
                adapterItem.submitList(it.map {
                    it.toSongLike()
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSongsLiked()
    }


}