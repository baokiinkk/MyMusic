package com.baokiin.mymusic.data.respository

import android.util.Log
import com.baokiin.mymusic.data.local.AppDao
import com.baokiin.mymusic.data.model.Song
import javax.inject.Inject


class RepositoryLocalImpl @Inject constructor(
    private val dao: AppDao
) : RepositoryLocal {
    override suspend fun getDataSong(): MutableList<Song> {
        Log.d("quocbao",dao.getDataSong().toString())
        return  dao.getDataSong()
    }

    override suspend fun insertSong(song: Song): Boolean =
        try {
            Log.d("quocbao",song.toString())
            dao.addSong(song)
            true
        } catch (e: Exception) {
            false
        }


    override suspend fun deleteSong(song: Song): Boolean =
        try {
            dao.deleteSong(song)
            true
        } catch (e: Exception) {
            false
        }

    override suspend fun updateSong(song: Song) =
        dao.updateSong(song)

    override suspend fun isDownload(id: String): Boolean =
        dao.isDownload(id) != null
}