package com.baokiin.mymusic.ui.info.offline


import android.util.Log
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.databinding.FragmentOfflineBinding
import com.baokiin.mymusic.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class OfflineFragment :BaseFragment<FragmentOfflineBinding>(){
    override fun getLayoutRes(): Int {
        return R.layout.fragment_offline
    }
    private lateinit var adapterItem: ItemPlayListAdapter
    private val viewModel by viewModels<OfflineViewModel>()
    override fun onCreateViews() {

        setup()
    }

    private fun setup() {
        adapterItem = ItemPlayListAdapter {
            EventBus.getDefault().post(EventBusModel.SongSingle(it))
        }
        baseBinding.apply {
            adapter = adapterItem
        }

        viewModel.songs.observe(viewLifecycleOwner,{
            it?.let {
                adapterItem.submitList(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSongs()
    }


}