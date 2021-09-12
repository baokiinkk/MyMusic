package com.baokiin.mymusic.data.model

import java.io.Serializable

data class DataApi(val message:String? = null,val data: Data?=null)
data class DataFind(val message: String? = null,val data:MutableList<Data>?=null)
data class Data(val song: MutableList<Song>,val items:MutableList<Song>,val info:Info? =null)
data class Info(val name:String,val thumbnail:String)
data class Song(
    val id: String,
    val name: String,
    val code: String,
    val artists_names: String? = null,
    val performer:String,
    val lyric:String,
    var thumbnail:String,
    var thumb:String,
    var song:String? = null
):Serializable




