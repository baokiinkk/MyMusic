package com.baokiin.mymusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.data.model.SongLike
import javax.inject.Singleton

@Singleton
@Database(entities = [Song::class,SongLike::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao(): AppDao // bat buoc co

}