package com.baokiin.mymusic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(private var list: MutableList<Fragment>, fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
    fun removeFragment(){
        list.forEach {
            it.onDestroy()
        }
    }
}