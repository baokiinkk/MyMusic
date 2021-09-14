package com.baokiin.mymusic.ui.trend

import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentTrendingBinding
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.BaseFragment
import org.greenrobot.eventbus.EventBus



class TrendingFragment : BaseFragment<FragmentTrendingBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_trending
    }

    //-------------------------------- Variable ----------------------------------------
    private lateinit var adapterItem: ItemPlayListAdapter
    private val viewModel by activityViewModels<HomeViewModel>()
    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setup()
        clickView()
    }



    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        adapterItem = ItemPlayListAdapter {it,_->
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        baseBinding.apply {
            adapter = adapterItem
            viewmodel = viewModel
        }
        viewModel.getData(requireContext())
        viewModel.trending.observe(viewLifecycleOwner,{
            it?.let {
               adapterItem.submitList(it.data?.song)
            }
        })

    }

    private fun startMediaService(song: Song) {
        EventBus.getDefault().post(EventBusModel.SongSingle(song))
    }

    private fun clickView() {
        baseBinding.btnPlay.setOnClickListener {
            EventBus.getDefault().post(
                EventBusModel.SongSingle(
                    adapterItem.currentList[0],
                    adapterItem.currentList.subList(1,adapterItem.itemCount)
                )
            )
        }
    }

}