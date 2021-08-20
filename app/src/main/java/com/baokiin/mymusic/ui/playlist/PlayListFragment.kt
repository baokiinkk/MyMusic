package com.baokiin.mymusic.ui.playlist

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentPlayListBinding
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.CATEGORY
import com.baokiin.mymusic.utils.Utils.KPOP
import com.baokiin.mymusic.utils.Utils.USUK
import com.baokiin.mymusic.utils.Utils.VPOP
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_play_list
    }

    //-------------------------------- Variable ----------------------------------------
    private lateinit var adapterItem: ItemPlayListAdapter

    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setup()
        clickView()
    }

    //-------------------------------------- recive data ------------------------------------------

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSecond(song: DataApi) {
        baseBinding.data = song
        adapterItem.submitList(song.data?.song ?: song.data?.items)
        baseBinding.btnPlay.setOnClickListener {
            EventBus.getDefault().post(
                EventBusModel.SongSingle(
                    adapterItem.currentList[0],
                    adapterItem.currentList.subList(1,adapterItem.itemCount)
                )
            )
        }
    }

    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        EventBus.getDefault().register(this)
        adapterItem = ItemPlayListAdapter {
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        baseBinding.apply {
            adapter = adapterItem
        }
    }

    private fun startMediaService(song: Song) {
        EventBus.getDefault().post(EventBusModel.SongSingle(song))
    }

    private fun clickView() {
        baseBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

}