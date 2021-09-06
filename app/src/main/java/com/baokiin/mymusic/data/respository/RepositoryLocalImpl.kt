package com.baokiin.mymusic.data.respository

import android.util.Log
import com.baokiin.mymusic.data.local.AppDao
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike
import javax.inject.Inject


class RepositoryLocalImpl @Inject constructor(
    private val dao: AppDao
) : RepositoryLocal {

    override suspend fun insertSong(song: Song): Boolean =
        try {
            dao.addSong(song)
            true
        } catch (e: Exception) {
            false
        }

    override suspend fun insertSongLike(song: SongLike): Boolean =
        try {
            dao.addSongLike(song)
            true
        } catch (e: Exception) {
            false
        }


    override suspend fun deleteAllSong() = dao.deleteAllSong()
    override suspend fun deleteAllSongLike() = dao.deleteAllSongLike()

    override suspend fun deleteSongLikeById(song: SongLike) = dao.deleteSongLike(song)

    override suspend fun getSongDownloaded(): MutableList<Song> = dao.getSongDownloaded()
    override suspend fun getSongLiked(): MutableList<SongLike> = dao.getSongLiked()

    override suspend fun getDataSongById(id: String): Song? = dao.getDataSong(id)
    override suspend fun getDataSongLikeById(id: String): SongLike? = dao.getDataSongLike(id)

}