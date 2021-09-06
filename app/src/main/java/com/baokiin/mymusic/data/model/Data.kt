package com.baokiin.mymusic.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class DataApi(val message: String? = null, val data: Data? = null)
data class DataFind(val message: String? = null, val data: MutableList<Data>? = null)
data class Data(val song: MutableList<Song>, val items: MutableList<Song>, val info: Info? = null)
data class Info(val name: String, val thumbnail: String)

@Entity
data class Song(
    @PrimaryKey val id: String,
    val name: String,
    val code: String? = null,
    val artists_names: String?,
    var lyric: String?,
    var thumbnail: String?,
    var thumb: String? = null,
    var song: String?,
) : Serializable {
    fun toSongLike(): SongLike {
        return SongLike(id, name, artists_names, lyric, thumbnail, song)
    }
}

@Entity
data class SongLike(
    @PrimaryKey val id: String,
    val name: String,
    val artists_names: String?,
    var lyric: String?,
    var thumbnail: String?,
    var song: String?
) : Serializable {
    fun toSong(): Song {
        return Song(
            id,
            name,
            artists_names = artists_names,
            lyric = lyric,
            thumbnail = thumbnail,
            song = song
        )
    }
}






