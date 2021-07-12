package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.DataApi
import javax.inject.Singleton


@Singleton
interface Repository{
   suspend fun getTrending(): DataApi
}