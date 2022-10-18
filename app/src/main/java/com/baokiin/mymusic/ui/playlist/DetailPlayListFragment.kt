package com.baokiin.mymusic.ui.playlist


import android.view.View
import androidx.fragment.app.viewModels
import coil.load
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.PlayList
import com.baokiin.mymusic.databinding.FragmentDetailPlayListBinding
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.CATEGORY
import com.baokiin.mymusic.utils.Utils.PLAYLIST
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class DetailPlayListFragment : BaseFragment<FragmentDetailPlayListBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_play_list
    }

    //-------------------------------- Variable ----------------------------------------
    private lateinit var adapterItem: ItemPlayListAdapter
    private val viewModel: PlayListViewModel by viewModels()

    //-------------------------------- createView ----------------------------------------
    override fun onCreateViews() {
        setup()
        clickView()
    }

    //-------------------------------------- recive data ------------------------------------------

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSecond(song: DataApi) {
        adapterItem.submitList(song.data)
    }

    //-------------------------------- Func ----------------------------------------
    private fun setup() {
        EventBus.getDefault().register(this)
        val category = arguments?.getString(CATEGORY)
        val playList = arguments?.getString(PLAYLIST)
        adapterItem = ItemPlayListAdapter { it, pos ->
            startMediaService(pos)
        }
        baseBinding.apply {
            adapter = adapterItem

        }
        if (playList == null)
            viewModel.getData(requireContext(), category)
        else {
            val data = Gson().fromJson(playList, PlayList::class.java)
            adapterItem.submitList(data.songResponses)
            if (data.songResponses?.isNotEmpty() == true) {
                val image = data.songResponses[0].thumbnail ?: ""
                baseBinding.view.load(image.replace("w94", "w480"))
            } else {
                baseBinding.textNonData.visibility = View.VISIBLE
                baseBinding.constraintLayout.visibility = View.GONE

            }
        }
        viewModel.liveData.observe(viewLifecycleOwner)
        {
            getData(it)
        }
    }

    private fun getData(dataApi: DataApi?) {
        dataApi?.let {
            adapterItem.submitList(it.data)
            val image = dataApi.data?.get(0)?.thumbnail ?: ""
            baseBinding.view.load(image.replace("w94", "w480"))
        }
    }

    private fun startMediaService(pos: Int) {
        EventBus.getDefault().post(
            EventBusModel.SongSingle(
                adapterItem.currentList[pos],
                adapterItem.currentList,
                pos
            )
        )
    }

    private fun clickView() {
        baseBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        baseBinding.btnPlay.setOnClickListener {
            startMediaService(0)
        }

    }

}