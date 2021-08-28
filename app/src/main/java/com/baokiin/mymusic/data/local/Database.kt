package com.baokiin.mymusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baokiin.mymusic.data.model.Song
import javax.inject.Singleton

@Singleton
@Database(entities = [Song::class], version = 123)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao(): AppDao // bat buoc co

}