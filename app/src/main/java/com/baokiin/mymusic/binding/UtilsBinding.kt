package com.baokiin.mymusic.binding

import android.widget.ImageView

import androidx.databinding.BindingAdapter

import coil.load

class UtilsBinding{
    companion object {
        @BindingAdapter("android:loadImageTitleHome")
        @JvmStatic
        fun loadImageTitleHome(view: ImageView, image: String?) {
            image?.let {
                view.load(it.replace("w94","w360"))
            }
        }
    }
}