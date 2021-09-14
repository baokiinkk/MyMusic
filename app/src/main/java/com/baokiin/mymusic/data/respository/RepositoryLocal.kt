package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike
import javax.inject.Singleton


@Singleton
interface RepositoryLocal {
    suspend fun getDataSongById(id:String): Song?
    suspend fun getDataSongLikeById(id:String): SongLike?

    suspend fun insertSong(song: Song): Boolean
    suspend fun insertSongLike(song: SongLike): Boolean

    suspend fun getSongDownloaded(): MutableList<Song>
    suspend fun getSongLiked(): MutableList<SongLike>

    suspend fun deleteAllSong()
    suspend fun deleteAllSongLike()
    suspend fun deleteSongLikeById(song: SongLike)
}