package com.baokiin.mymusic.binding

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import kotlinx.android.synthetic.main.play_music.view.*


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
        @SuppressLint("ClickableViewAccessibility")
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