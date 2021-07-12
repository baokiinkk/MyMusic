package com.baokiin.mymusic.binding

import android.widget.ImageView

import androidx.databinding.BindingAdapter

import coil.load

class UtilsBinding{
    companion object {
        @BindingAdapter("android:loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, image: Int?) {
            image?.let {
                view.load(it)
            }
        }
    }
}