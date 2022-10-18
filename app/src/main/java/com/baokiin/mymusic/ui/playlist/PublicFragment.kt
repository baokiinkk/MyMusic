package com.baokiin.mymusic.ui.playlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPublicPlayListAdapter
import com.baokiin.mymusic.databinding.FragmentPublicBinding
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PublicFragment : BaseFragment<FragmentPublicBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_public

    private val viewModel: PlayListViewModel by viewModels()
    private val adapterItem: ItemPublicPlayListAdapter by lazy {
        ItemPublicPlayListAdapter { playList, i ->
            val bundle = Bundle().apply { putString(Utils.PLAYLIST,Gson().toJson(playList)) }
            val frament = DetailPlayListFragment().apply { arguments = bundle }
            Utils.gotoFragment(requireActivity(),frament,true)
        }
    }

    override fun onCreateViews() {
        val type = arguments?.getString(Utils.TYPE_PLAYLIST)
        if(type == Utils.PRIVATE_PLAYLIST) {
            baseBinding.btnAddPlayList.visibility = View.VISIBLE

        }
        viewModel.getPublicPlayList(requireContext(),type)
        viewModel.publicPlayListliveData.observe(viewLifecycleOwner) {
            it?.let {
                adapterItem.submitList(it)
            }
        }
        viewModel.createPlayListliveData.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.getPublicPlayList(requireContext(),type)
            }
        }
        baseBinding.adapter = adapterItem
        clickView()
    }

    private fun clickView() {
        baseBinding.apply {
            btnAddPlayList.setOnClickListener {
                Utils.confirmDialog(requireActivity()){
                    viewModel.createPlayList(requireContext(),it)
                }
            }
        }
    }

}