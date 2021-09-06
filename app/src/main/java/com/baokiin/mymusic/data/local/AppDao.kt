package com.baokiin.mymusic.data.local

import androidx.room.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike


@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: Song)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongLike(song: SongLike)

    @Delete
    suspend fun deleteSongLike(vararg song: SongLike)

    @Query("DELETE FROM Song")
    suspend fun deleteAllSong()

    @Query("DELETE FROM SongLike")
    suspend fun deleteAllSongLike()

    @Query("SELECT * FROM Song WHERE Song.id=:id")
    suspend fun getDataSong(id:String): Song?

    @Query("SELECT * FROM SongLike WHERE SongLike.id=:id")
    suspend fun getDataSongLike(id:String): SongLike?

    @Query("SELECT * FROM Song")
    suspend fun getSongDownloaded(): MutableList<Song>

    @Query("SELECT * FROM SongLike" )
    suspend fun getSongLiked(): MutableList<SongLike>

}