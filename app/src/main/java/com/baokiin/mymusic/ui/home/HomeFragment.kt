package com.baokiin.mymusic.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.ui.playlist.PlayListFragment
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.KPOP
import com.baokiin.mymusic.utils.Utils.USUK
import com.baokiin.mymusic.utils.Utils.VPOP
import com.baokiin.mymusic.utils.Utils.diaLogBottom
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    //-------------------------------- Variable ----------------------------------------
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var itemtrendHomeAdapter: ItemHomeTitleAdapter
    private lateinit var itemamericaHomeAdapter: ItemHomeAdapter
    private lateinit var itemkpopHomeAdapter: ItemHomeAdapter
    private lateinit var itemvpopHomeAdapter: ItemHomeAdapter

    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setUpAdapter()
        getData()
        setup()
        clickView()
    }

    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        baseBinding.apply {
            viemodel = viewModel
            itemtrendhomeadapter = itemtrendHomeAdapter
            itemamericahomeadapter = itemamericaHomeAdapter
            itemkpophomeadapter = itemkpopHomeAdapter
            itemvpophomeadapter = itemvpopHomeAdapter

        }

    }


    //-------------------------------- Data ----------------------------------------
    private fun getData() {
        viewModel.apply {
            viewModel.getData()
            trending.observe(viewLifecycleOwner, {
                it?.data?.let {
                    itemtrendHomeAdapter.submitList(it.song.subList(0, 6))
                    baseBinding.viewPagerTitle.currentItem = 1
                }
            })
            america.observe(viewLifecycleOwner, {
                it?.data?.let { itemamericaHomeAdapter.submitList(it.items.subList(0, 6)) }
            })
            kpop.observe(viewLifecycleOwner, {
                it?.data?.let { itemkpopHomeAdapter.submitList(it.items.subList(0, 6)) }
            })
            vpop.observe(viewLifecycleOwner, {
                it?.data?.let { itemvpopHomeAdapter.submitList(it.items.subList(0, 6)) }
            })
        }
    }

    private fun clickView() {
        baseBinding.vpopLayout.btnMore.setOnClickListener {
            gotoPlayList(viewModel.vpop.value)
        }
        baseBinding.kpopLayout.btnMore.setOnClickListener {
            gotoPlayList(viewModel.kpop.value)
        }
        baseBinding.amedicaLayout.btnMore.setOnClickListener {
            gotoPlayList(viewModel.america.value)
        }
    }
    private fun gotoPlayList(song:DataApi?){
        EventBus.getDefault().post(song)
        EventBus.getDefault().post(ShowFrament(true))
    }
    private fun setUpAdapter() {
        itemtrendHomeAdapter = ItemHomeTitleAdapter {
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemamericaHomeAdapter = ItemHomeAdapter {
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemkpopHomeAdapter = ItemHomeAdapter {
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemvpopHomeAdapter = ItemHomeAdapter {
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
    }

    private fun startMediaService(song: Song) {
        EventBus.getDefault().post(SongSingle(song))
    }

}