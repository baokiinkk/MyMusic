package com.baokiin.mymusic.data.model

import android.media.MediaPlayer
import java.io.Serializable

data class DataApi(val message:String? = null,val data: Data?=null)
data class Data(val song: MutableList<Song>,val items:MutableList<Song>,val info:Info? =null)
data class Info(val name:String,val thumbnail:String)
data class Song(
    val id: String,
    val name: String,
    val code: String,
    val artists_names: String,
    val performer:String,
    val lyric:String,
    val thumbnail:String,
    var song:String? = null
):Serializable




