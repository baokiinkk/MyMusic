package com.baokiin.mymusic.binding


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemPlayListAdapter


class RecycleViewBinding {
    companion object {
        @BindingAdapter("android:adapter_recycle")
        @JvmStatic
        fun recycleView(view: RecyclerView, adapter: ItemHomeAdapter) {
            view.apply {
                this.adapter = adapter
                layoutManager = GridLayoutManager(
                    view.context,
                    1,
                    GridLayoutManager.HORIZONTAL, false
                )
            }
        }
        @BindingAdapter("android:adapter")
        @JvmStatic
        fun recycleViews(view: RecyclerView, adapter: ItemPlayListAdapter) {
            view.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(view.context)
            }
        }
    }
}