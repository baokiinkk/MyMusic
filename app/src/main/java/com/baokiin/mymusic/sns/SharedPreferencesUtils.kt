package com.baokiin.mymusic.sns

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

object SharedPreferencesUtils {
    const val TOKEN_ID: String = "TokenID"
    fun setTokenID(context: FragmentActivity, token: String?) {
        val sharedPreference: SharedPreferences = context.getSharedPreferences(
            TOKEN_ID, Context.MODE_PRIVATE
        )
        sharedPreference.edit().putString(TOKEN_ID, token).apply()
    }
    fun getTokenID(context: AppCompatActivity): String =
        context.getSharedPreferences(TOKEN_ID, Context.MODE_PRIVATE).getString(TOKEN_ID,"") ?:""

    fun AppCompatActivity.isFirstTimeAskingPermission(permission: String): Boolean {
        return this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
            .getBoolean(permission, true)
    }


}