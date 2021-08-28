package com.baokiin.mymusic.data.local

import androidx.room.*
import com.baokiin.mymusic.data.model.Song


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(song: Song)

    @Delete
    suspend fun deleteSong(vararg song: Song)

    @Query("SELECT * FROM Song")
    suspend fun getDataSong(): MutableList<Song>

    @Query("SELECT * FROM Song WHERE Song.id=:id")
    suspend fun isDownload(id: String): Song?

    @Update
    suspend fun updateSong(song: Song)


}