package com.baokiin.mymusic.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.baokiin.mymusic.R


object Utils {
    const val TAG = "quocbao"



    const val SNS_RESULT_DATA = "SNS_RESULT_DATA"

    const val SNS_LOGIN_TYPE = "SNS_LOGIN_TYPE"
    const val USER = "user"
    var TOKEN = "token"
    const val STUDENT = "student"
    const val TEACHER = "teacher"
    const val SNS_REQUEST_CODE_GOOGLE = 887

    const val SNS_REQUEST_CODE_PHONE = 886

    fun gotoFragment(activity: FragmentActivity,fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}