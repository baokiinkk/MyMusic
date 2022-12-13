package com.baokiin.mymusic.ui.search

import android.annotation.SuppressLint
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentSearchBinding
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class SearchFragment :BaseFragment<FragmentSearchBinding>(){
    override fun getLayoutRes(): Int {
        return R.layout.fragment_search
    }
    //-------------------------------- Variable ----------------------------------------
    private lateinit var adapterItem: ItemPlayListAdapter
    private val viewModel by viewModels<SearchViewModel>()


    override fun onCreateViews() {
        setup()
        clickView()
    }
    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        adapterItem = ItemPlayListAdapter {it,_->
            hideKeyboard()
            startMediaService(it)
        }
        viewModel.trending.observe(viewLifecycleOwner) {
            it?.let {
                adapterItem.submitList(it)
            }
        }
        baseBinding.apply {
            adapter = adapterItem
            viewmodel = viewModel
            activity = requireActivity()
        }
    }
    private fun startMediaService(song: Song) {
        EventBus.getDefault().post(
            EventBusModel.SongSingle(
                song,
                adapterItem.currentList.subList(0,1),
                0
            )
        )
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun clickView(){
        baseBinding.recyclerViewPlayList.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
        baseBinding.edtSearch.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.getData(text.toString())
        }
        baseBinding.btnClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}