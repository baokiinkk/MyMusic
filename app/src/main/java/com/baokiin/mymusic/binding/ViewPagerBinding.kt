package com.baokiin.mymusic.binding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.baokiin.mymusic.binding.ViewPagerBinding.Companion.tabLayout
import com.baokiin.mymusic.binding.ViewPagerBinding.Companion.viewpager
import com.baokiin.mymusic.ui.music.MusicFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs


class ViewPagerBinding {
    companion object {
        @BindingAdapter("android:adapter")
        @JvmStatic
        fun tabLayout(view: ViewPager2, adapter: ItemHomeTitleAdapter) {
            view.apply {
                this.adapter = adapter
                offscreenPageLimit = 1
                val cpt = CompositePageTransformer()
                cpt.addTransformer{ page, position ->
                    val myOffset: Float = position * -(2 * 20f + 40f)
                    when {
                        position < -1 -> {
                            page.translationX = -myOffset
                        }
                        position <= 1 -> {
                            val scaleFactor =
                                0.7f.coerceAtLeast(1 - abs(position - 0.14285715f))
                            page.translationX = myOffset
                            page.scaleY = scaleFactor
                            page.alpha = scaleFactor
                        }
                        else -> {
                            page.alpha = 0f
                            page.translationX = myOffset
                        }
                    }
                }
                cpt.addTransformer(MarginPageTransformer(80))
                setPageTransformer(cpt)
            }
        }

        @BindingAdapter("android:adapter")
        @JvmStatic
        fun viewpager(view: ViewPager2, adapter: ItemHomeAdapter) {
            view.apply {
                this.adapter = adapter
                offscreenPageLimit = 2
                val cpt = CompositePageTransformer()
                cpt.addTransformer(MarginPageTransformer(20))
                setPageTransformer(cpt)
            }
        }

        @BindingAdapter("android:tab_layout","android:adapter_viewpager2")
        @JvmStatic
        fun viewpager(view: ViewPager2, tabLayout: TabLayout,adapter: ViewPageAdapter) {
            view.adapter = adapter
            view.isUserInputEnabled = false
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