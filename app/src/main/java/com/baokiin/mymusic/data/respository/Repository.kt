package com.baokiin.mymusic.data.respository

import com.baokiin.mymusic.data.model.LoginUser

import com.baokiin.mymusic.data.model.Users
import javax.inject.Singleton


@Singleton
interface Repository{
   suspend fun login(): Users
}