package com.baokiin.mymusic.ui.login

import android.util.Log
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemTitleHomeAdapter
import com.baokiin.mymusic.databinding.FragmentHomeBinding
import com.baokiin.mymusic.databinding.ItemTitleHomeBinding
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }


    //-------------------------------- Variable ----------------------------------------
    val viewModel by viewModels<HomeViewModel>()
    lateinit var itemTitleHomeAdapter:ItemTitleHomeAdapter


    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setup()
        getData()
        clickView()
    }

    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        itemTitleHomeAdapter = ItemTitleHomeAdapter{

        }
        baseBinding.apply {
            viemodel = viewModel
            itemhomeadapter =itemTitleHomeAdapter
        }

    }

    //-------------------------------- Data ----------------------------------------
    private fun getData() {
        viewModel.apply {
            viewModel.getTrending()
            songs.observe(viewLifecycleOwner, {
                it?.let {
                    val song = it.song.subList(0,6)
                    itemTitleHomeAdapter.submitList(song)
                }
            })
        }
    }

    private fun clickView() {

    }

}