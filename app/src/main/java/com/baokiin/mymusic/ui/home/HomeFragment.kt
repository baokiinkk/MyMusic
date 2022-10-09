package com.baokiin.mymusic.ui.home


import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.ui.info.InfoFragment
import com.baokiin.mymusic.ui.playlist.PlayListFragment
import com.baokiin.mymusic.ui.search.SearchFragment
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.CATEGORY
import com.baokiin.mymusic.utils.Utils.KPOP
import com.baokiin.mymusic.utils.Utils.USUK
import com.baokiin.mymusic.utils.Utils.VPOP
import com.baokiin.mymusic.utils.Utils.gotoFragment
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


    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        baseBinding.apply {
            viemodel = viewModel
            viewModel.getData(requireContext())
            itemtrendhomeadapter = itemtrendHomeAdapter
            itemamericahomeadapter = itemamericaHomeAdapter
            itemkpophomeadapter = itemkpopHomeAdapter
            itemvpophomeadapter = itemvpopHomeAdapter
        }

    }


    //-------------------------------- Data ----------------------------------------
    private fun getData() {
        viewModel.apply {
            trending.observe(viewLifecycleOwner) {
                it?.data?.let {
                    itemtrendHomeAdapter.submitList(it.subList(0, 6))
                    baseBinding.viewPagerTitle.currentItem = 1
                }
            }
            america.observe(viewLifecycleOwner) { song ->
                song?.data?.let { itemamericaHomeAdapter.submitList(it.subList(0, 6)) }
            }
            kpop.observe(viewLifecycleOwner) { song ->
                song?.data?.let { itemkpopHomeAdapter.submitList(it.subList(0, 6)) }
            }
            vpop.observe(viewLifecycleOwner) { song ->
                song?.data?.let { itemvpopHomeAdapter.submitList(it.subList(0, 6)) }
            }
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
        baseBinding.profile.setOnClickListener {
            gotoFragment(requireActivity(),InfoFragment(),true)
        }
    }
    private fun gotoPlayList(song:String){
        val bundle = Bundle().apply { putString(CATEGORY,song) }
        val frament = PlayListFragment().apply { arguments = bundle }
        gotoFragment(requireActivity(),frament,true)
    }
    private fun gotoSearch(){
        gotoFragment(requireActivity(),SearchFragment(),true)

    }
    private fun setUpAdapter() {
        itemtrendHomeAdapter = ItemHomeTitleAdapter {
            startMediaService(itemtrendHomeAdapter.currentList[it],itemtrendHomeAdapter.currentList,it)
        }
        itemamericaHomeAdapter = ItemHomeAdapter {
            startMediaService(itemamericaHomeAdapter.currentList[it],itemamericaHomeAdapter.currentList,it)
        }
        itemkpopHomeAdapter = ItemHomeAdapter {
            startMediaService(itemkpopHomeAdapter.currentList[it],itemkpopHomeAdapter.currentList,it)
        }
        itemvpopHomeAdapter = ItemHomeAdapter {
            startMediaService(itemvpopHomeAdapter.currentList[it],itemvpopHomeAdapter.currentList,it)
        }
    }

    private fun startMediaService(song: Song,listSong:MutableList<Song>,pos:Int) {
        EventBus.getDefault().post(
            SongSingle(
                song,
                listSong,
                pos
            )
        )
    }

}