package com.baokiin.mymusic.ui.music


import androidx.fragment.app.activityViewModels
import com.baokiin.mymusic.R
import com.baokiin.mymusic.databinding.FragmentMusicBinding
import com.baokiin.mymusic.ui.activity.MainViewModel
import com.baokiin.mymusic.utils.BaseFragment

class MusicFragment : BaseFragment<FragmentMusicBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_music
    }
    private val viewModel by activityViewModels<MainViewModel>()
    override fun onCreateViews() {
        baseBinding.viewmodel = viewModel
    }

}