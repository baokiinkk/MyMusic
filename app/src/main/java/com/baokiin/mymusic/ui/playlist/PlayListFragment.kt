package com.baokiin.mymusic.ui.playlist

import android.os.Bundle
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.databinding.FragmentPlayListBinding
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_play_list

    override fun onCreateViews() {
        baseBinding.apply {
            adapter = ViewPageAdapter(
                mutableListOf(
                    PublicFragment().apply {
                    val bundle = Bundle().apply { putString(Utils.TYPE_PLAYLIST, Utils.PUBLIC_PLAYLIST) }
                    arguments = bundle
                }, PublicFragment().apply {
                    val bundle = Bundle().apply { putString(Utils.TYPE_PLAYLIST, Utils.PRIVATE_PLAYLIST) }
                    arguments = bundle
                }),
                requireActivity()
            )

        }
        clickView()
    }

    private fun clickView() {
        baseBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}