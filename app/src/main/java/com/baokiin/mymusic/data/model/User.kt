package com.baokiin.mymusic.data.model

data class LoginUser(val id: String, val password: String)
data class Users(val count:String? = null,val message:String? = null,val data:Data? = null)
data class Items(
    val id: String,
    val name: String,
)
data class Data(val items: MutableList<Items>? = null)


