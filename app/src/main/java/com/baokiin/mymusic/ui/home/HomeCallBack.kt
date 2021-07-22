package com.baokiin.mymusic.ui.home

import android.graphics.Bitmap
import com.baokiin.mymusic.data.model.Song

interface HomeCallBack {
    fun clickItem(song: Song,bitmap: Bitmap)
}