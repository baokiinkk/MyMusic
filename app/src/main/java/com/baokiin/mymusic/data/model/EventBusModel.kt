package com.baokiin.mymusic.data.model

import android.media.MediaPlayer
import okhttp3.ResponseBody

class EventBusModel {
    data class MediaInfo(val song:Song,val mediaPlayer: MediaPlayer)
    data class Times(val time:Int)
    data class TimesLong(val time:Long)
    data class ShowFrament(val boolean:Boolean)
    data class DownloadMp3(val song:Song)
    data class SongSingle(val song: Song,val isList:MutableList<Song>? = null)
    data class Reponse(val reponseBody: ResponseBody,val song:Song)
}