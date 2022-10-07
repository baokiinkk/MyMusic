package com.baokiin.mymusic.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.ui.playlist.PlayListFragment
import com.baokiin.mymusic.ui.search.SearchFragment
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.CATEGORY
import com.baokiin.mymusic.utils.Utils.KPOP
import com.baokiin.mymusic.utils.Utils.USUK
import com.baokiin.mymusic.utils.Utils.VPOP
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    //-------------------------------- Variable ----------------------------------------
    private val viewModel by activityViewModels<HomeViewModel>()
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

    override fun onDestroy() {
        super.onDestroy()
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
            gotoPlayList(VPOP)
        }
        baseBinding.kpopLayout.btnMore.setOnClickListener {
            gotoPlayList(KPOP)
        }
        baseBinding.amedicaLayout.btnMore.setOnClickListener {
            gotoPlayList(USUK)
        }
        baseBinding.btnSearch.setOnClickListener {
            gotoSearch()
        }
    }
    private fun gotoPlayList(song:String){
        val bundle = Bundle().apply { putString(CATEGORY,song) }
        val frament = PlayListFragment().apply { arguments = bundle }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.containerFramgnet,frament)
            .commit()
        EventBus.getDefault().post(ShowFragment(true))
    }
    private fun gotoSearch(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.containerFramgnet,SearchFragment())
            .commit()
        EventBus.getDefault().post(ShowFragment(true))
    }
    private fun setUpAdapter() {
        itemtrendHomeAdapter = ItemHomeTitleAdapter {
            val url = "https://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemamericaHomeAdapter = ItemHomeAdapter {
            val url = "https://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemkpopHomeAdapter = ItemHomeAdapter {
            val url = "https://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
        itemvpopHomeAdapter = ItemHomeAdapter {
            val url = "https://api.mp3.zing.vn/api/streaming/audio/${it.id}/320"
            it.song = url
            startMediaService(it)
        }
    }

    private fun startMediaService(song: Song) {
        EventBus.getDefault().post(SongSingle(song))
    }

}