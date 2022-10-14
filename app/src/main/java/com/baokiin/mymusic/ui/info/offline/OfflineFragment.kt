package com.baokiin.mymusic.ui.info.offline


import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemSongLikeAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentSongLocalBinding
import com.baokiin.mymusic.ui.info.InfoViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class OfflineFragment : BaseFragment<FragmentSongLocalBinding>() {
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
        viewModel.getSongsIsDownloaded()
    }

    private fun setup() {
        EventBus.getDefault().register(this)
        adapterItem = ItemSongLikeAdapter { song, index ->
            EventBus.getDefault()
                .post(EventBusModel.SongSingle(song.toSong(), adapterItem.currentList.map {
                    it.toSong()
                }.toMutableList(), index))
        }
        baseBinding.apply {
            adapter = adapterItem
            val itemTouchHelper = ItemTouchHelper(
                Utils.callBackSwipe(
                    recycleviewLocal,
                    adapterItem
                ){list,_->
                    viewModel.updateDataSong(list,false)
                }
            )
            itemTouchHelper.attachToRecyclerView(recycleviewLocal)
        }
        viewModel.songIsDownloaded.observe(viewLifecycleOwner, { listSong ->
            listSong?.let { list ->
                adapterItem.submitList(list.map {
                    it.toSongLike()
                })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSongsIsDownloaded()
    }



}