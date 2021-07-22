package com.baokiin.mymusic.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemHomeAdapter
import com.baokiin.mymusic.adapter.ItemHomeTitleAdapter
import com.baokiin.mymusic.adapter.SliderTransformer
import com.baokiin.mymusic.adapter.ViewPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
                    val myOffset: Float = position * -(2 * 20f + 30f)
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
                cpt.addTransformer(MarginPageTransformer(20))
                setPageTransformer(cpt)
            }
//
//            GlobalScope.launch(Dispatchers.Main) {
//                var index = 0
//                while (true) {
//                    delay(5000)
//                    if (index < adapter.itemCount - 1)
//                        index++
//                    else
//                        index = 0
//                    view.setCurrentItem(index, true)
//                }
//
//            }
        }

        @BindingAdapter("android:adapter")
        @JvmStatic
        fun viewpager(view: ViewPager2, adapter: ItemHomeAdapter) {
            view.apply {
                this.adapter = adapter
                offscreenPageLimit = 3
                setPageTransformer(SliderTransformer(20))
            }
        }



    }
}