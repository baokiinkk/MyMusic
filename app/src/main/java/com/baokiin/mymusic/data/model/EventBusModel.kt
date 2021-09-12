package com.baokiin.mymusic.data.model

import android.media.MediaPlayer

class EventBusModel {
    data class MediaInfo(val song:Song,val mediaPlayer: MediaPlayer)
    data class Times(val time:Int)
    data class TimesLong(val time:Long)
    data class ShowFrament(val boolean:Boolean)
    data class ListSong(val song:MutableList<Song>)
    data class SongSingle(val song: Song,val isList:MutableList<Song>? = null)
}