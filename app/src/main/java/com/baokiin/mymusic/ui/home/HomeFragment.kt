package com.baokiin.mymusic.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.data.model.MediaInfo
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.ui.service.MediaService
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
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
                it?.let {
                    itemtrendHomeAdapter.submitList(it)
                    baseBinding.viewPagerTitle.currentItem = 1
                }
            })
            america.observe(viewLifecycleOwner, {
                it?.let { itemamericaHomeAdapter.submitList(it) }
            })
            kpop.observe(viewLifecycleOwner, {
                it?.let { itemkpopHomeAdapter.submitList(it) }
            })
            vpop.observe(viewLifecycleOwner, {
                it?.let { itemvpopHomeAdapter.submitList(it) }
            })
        }
    }

    private fun clickView() {

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
        EventBus.getDefault().post(song)
    }

}