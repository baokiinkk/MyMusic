package com.baokiin.mymusic.binding

import android.net.Uri
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2

import coil.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemPlayListAdapter
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.data.model.DataApi
import com.baokiin.mymusic.data.model.Song
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UtilsBinding {
    companion object {
        @BindingAdapter("android:loadImageTitleHome")
        @JvmStatic
        fun loadImageTitleHome(view: ImageView, image: String?) {
            image?.let {
                view.load(
                    it.replace("w94", "w480").replace("w165","w480")
                )
            }
        }
        @BindingAdapter("android:loadImageMusic")
        @JvmStatic
        fun loadImageMusic(view: ImageView, image: String?) {
            image?.let {
                view.load(
                    it.replace("w94", "w480")
                )
            }
        }

        @BindingAdapter("android:loadImagePlayMusic")
        @JvmStatic
        fun loadImagePlayMusic(view: ImageView, image: String?) {
            image?.let {
                view.load(it.replace("w94", "w360")) {
                    transformations(BlurTransformation(view.context, 15f, 3f))
                }

            }
        }

        @BindingAdapter("android:loadImageHome")
        @JvmStatic
        fun loadImageHome(view: ImageView, image: String?) {
            image?.let {
                view.load(it.replace("w94", "w360")) {
                    transformations(RoundedCornersTransformation(40f))
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
        @BindingAdapter("android:loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, image: Uri?) {
            image?.let {
                view.load(image)
            }
        }


        @BindingAdapter("android:adapter", "android:tab_layout")
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