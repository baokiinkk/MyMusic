package com.baokiin.mymusic.data.model

import android.media.MediaPlayer
import okhttp3.ResponseBody

class EventBusModel {
    data class MediaInfo(val song:Song,val mediaPlayer: MediaPlayer)
    data class Times(val time:Int)
    data class TimesLong(val time:Long)
    data class DownloadMp3(val song:Song)
    data class Songs(val song:MutableList<Song>,val index: Int?=null)
    data class SongSingle(val song: Song,val isList:MutableList<Song>? = null,var index:Int? = null)
    data class Response(val reponseBody: ResponseBody, val song:Song, val lyric:String, val img:String)
    data class LoadLocal(val boolean: Boolean)
    data class DataChange(val status:String)
    data class OnBackEvent(val status:Boolean)
    data class OnBackPlayMusicEvent(val status:Boolean)
    data class ProcessDownload(val status:Int)
}