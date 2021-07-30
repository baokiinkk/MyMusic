package com.baokiin.mymusic.binding

import android.widget.ImageView

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

import coil.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UtilsBinding{
    companion object {
        @BindingAdapter("android:loadImageTitleHome")
        @JvmStatic
        fun loadImageTitleHome(view: ImageView, image: String?) {
            image?.let {
                view.load(it.replace("w94","w360"))
            }
        }
        @BindingAdapter("android:loadImageHome")
        @JvmStatic
        fun loadImageHome(view: ImageView, image: String?) {
            image?.let {
                view.load(it.replace("w94","w360")){
                    transformations(RoundedCornersTransformation(25f))
                }
            }
        }
        @BindingAdapter("android:loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, image: Int?) {
            image?.let {
                view.load(image)
            }
        }

        @BindingAdapter("android:adapter","android:tab_layout")
        @JvmStatic
        fun viewpagerr(view: ViewPager2, tabLayout: TabLayout, adapter: ViewPageAdapter) {
            view.adapter = adapter
            TabLayoutMediator(
                tabLayout,
                view
            ) { tab, pos ->
                when (pos) {
                    0 -> {
                        tab.setIcon(R.drawable.ic_home)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_trend)
                    }

                    2 -> {
                        tab.setIcon(R.drawable.ic_account)
                    }
                }
            }.attach()
        }
    }
}