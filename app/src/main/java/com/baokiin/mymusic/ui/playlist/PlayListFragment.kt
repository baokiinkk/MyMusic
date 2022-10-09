package com.baokiin.mymusic.ui.playlist


import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.databinding.FragmentPlayListBinding
import com.baokiin.mymusic.ui.home.HomeViewModel
import com.baokiin.mymusic.utils.BaseFragment
import com.baokiin.mymusic.utils.Utils.CATEGORY
import com.baokiin.mymusic.utils.Utils.KPOP
import com.baokiin.mymusic.utils.Utils.VPOP
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class PlayListFragment : BaseFragment<FragmentPlayListBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_play_list
    }

    //-------------------------------- Variable ----------------------------------------
    private lateinit var adapterItem: ItemPlayListAdapter
    private val viewModel by activityViewModels<HomeViewModel>()
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
        val category = arguments?.get(CATEGORY)
        adapterItem = ItemPlayListAdapter {it,pos->
            startMediaService(pos)
        }
        baseBinding.apply {
            adapter = adapterItem

        }
        viewModel.getData(requireContext())
        when(category){
            VPOP->{
                viewModel.vpop.observe(viewLifecycleOwner) {
                    getData(it)
                }
            }
            KPOP->{
                viewModel.kpop.observe(viewLifecycleOwner) {
                    getData(it)
                }
            }
            else->{
                viewModel.america.observe(viewLifecycleOwner) {
                    getData(it)
                }
            }
        }
    }
    private fun getData(dataApi: DataApi?){
        dataApi?.let {
            adapterItem.submitList(it.data)
            baseBinding.data = it
        }
    }
    private fun startMediaService(pos:Int) {
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