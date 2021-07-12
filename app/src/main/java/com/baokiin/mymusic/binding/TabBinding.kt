package com.baokiin.mymusic.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.baokiin.mymusic.adapter.ItemTitleHomeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TabBinding {
    companion object {
        @BindingAdapter("android:tabLayoutId", "android:adapter")
        @JvmStatic
        fun tabLayout(view: ViewPager2, tabLayout: TabLayout, adapter: ItemTitleHomeAdapter) {
            view.adapter = adapter
            TabLayoutMediator(
                tabLayout,
                view
            ) { _, _ ->

            }.attach()
            GlobalScope.launch(Dispatchers.Main) {
                var index = 0
                while (true) {
                    delay(3000)
                    if (index < adapter.itemCount - 1)
                        index++
                    else
                        index = 0
                    view.setCurrentItem(index, true)
                }

            }
        }
    }
}