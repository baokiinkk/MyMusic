package com.baokiin.mymusic.binding

import android.content.res.Resources
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.adapter.ItemHomeAdapter


class RecycleViewBinding {
    companion object {
        @BindingAdapter("android:adapter_recycle")
        @JvmStatic
        fun recycleView(view: RecyclerView, adapter: ItemHomeAdapter) {
            val widthDevice = Resources.getSystem().displayMetrics.widthPixels
            view.apply {
                setPadding(0,0,widthDevice/2-30,0)
                this.adapter = adapter
                layoutManager = GridLayoutManager(
                    view.context,
                    1,
                    GridLayoutManager.HORIZONTAL, false
                )
            }
        }
    }
}