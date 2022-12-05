package com.baokiin.mymusic.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class DataApi(val message: String? = null, val data: MutableList<Song>? = null)
data class DataPlayListApi(val message: String? = null, val data: MutableList<PlayList>? = null)
data class DataFind(val message: String? = null, val data: MutableList<Data>? = null)
data class Data(val song: MutableList<Song>?)
data class UserStatus(val status: String?)


data class PlayList(
    val playlistId:Int,
    val name:String,
    val songResponses:MutableList<Song>?,
    val thumbnail:String
)
@Entity
data class Song(
    @PrimaryKey val songId: String,
    val name: String,
    val code: String? = null,
    val artistName: String?,
    var lyric: String?,
    var thumbnail: String?,
    var thumb: String? = null,
    var link: String?,
    var isLiked: Boolean?,
) : Serializable {
    fun toSongLike(): SongLike {
        return SongLike(songId, name, artistName, lyric, thumbnail, link,isLiked)
    }
}

@Entity
data class SongLike(
    @PrimaryKey val id: String,
    val name: String,
    val artists_names: String?,
    var lyric: String?,
    var thumbnail: String?,
    var link: String?,
    var isLiked: Boolean?
) : Serializable {
    fun toSong(): Song {
        return Song(
            id,
            name,
            artistName = artists_names,
            lyric = lyric,
            thumbnail = thumbnail,
            link = link,
            isLiked = isLiked
        )
    }
}






