package com.baokiin.mymusic.ui.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*


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
    private lateinit var onClickItem: HomeCallBack

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

    //-------------------------------- CallBack ----------------------------------------
    fun clickCallBack(callBack: HomeCallBack) {
        onClickItem = callBack
    }

    //-------------------------------- Data ----------------------------------------
    private fun getData() {
        viewModel.apply {
            viewModel.getData()
            trending.observe(viewLifecycleOwner, {
                it?.let { itemtrendHomeAdapter.submitList(it) }
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
        itemtrendHomeAdapter = ItemHomeTitleAdapter {it,bitmap->
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            onClickItem.clickItem(it,bitmap)
        }
        itemamericaHomeAdapter = ItemHomeAdapter {it,bitmap->
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            onClickItem.clickItem(it,bitmap)
        }
        itemkpopHomeAdapter = ItemHomeAdapter {it,bitmap->
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            onClickItem.clickItem(it,bitmap)
        }
        itemvpopHomeAdapter = ItemHomeAdapter {it,bitmap->
            val url = "http://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            onClickItem.clickItem(it,bitmap)
        }
    }

}