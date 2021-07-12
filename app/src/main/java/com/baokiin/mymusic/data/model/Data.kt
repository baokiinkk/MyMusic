package com.baokiin.mymusic.data.model

data class DataApi(val message:String? = null,val data: Data?=null)
data class Data(val song: MutableList<Song>)
data class Song(
    val id: String,
    val name: String,
    val code: String,
    val artists_names: String,
    val performer:String,
    val lyric:String,
    val thumbnail:String,
)



