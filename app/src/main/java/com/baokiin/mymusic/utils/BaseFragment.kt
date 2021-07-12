package com.baokiin.mymusic.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment



abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {


    // Tạo ra baseBinding để sử dụng
    protected lateinit var baseBinding: VB

    // // Return lại layout id
    abstract fun getLayoutRes(): Int

    // Cài đặt của View ở đây để tránh việc làm rồi onCreate
    abstract fun onCreateViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        baseBinding.lifecycleOwner = this
        onCreateViews()
        return baseBinding.root

    }


    // Tạo Kclass để sử dụng
}