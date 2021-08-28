package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.Song
import javax.inject.Singleton


@Singleton
interface RepositoryLocal{
   suspend fun getDataSong():MutableList<Song>
   suspend fun insertSong(song:Song):Boolean
   suspend fun deleteSong(song:Song):Boolean
   suspend fun updateSong(song: Song)
   suspend fun isDownload(id:String):Boolean
}