package com.baokiin.mymusic.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.R
import com.google.android.material.bottomsheet.BottomSheetDialog


object Utils {
    const val TAG = "quocbao"

    const val ACTION_PLAY = 1
    const val ACTION_STOP = 2
    const val ACTION = "action"
    const val ACTION_NEXT = 4
    const val ACTION_PREV = 3
    const val CHANNEL_ID = "123456"
    const val SONG = "Song"
    const val VPOP = "vpop"
    const val KPOP ="kpop"
    const val USUK = "usuk"
    const val TREND = "trend"
    const val CATEGORY = "tagegory"


    fun gotoFragment(activity: FragmentActivity,fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
    fun gotoFragment(activity: FragmentActivity, fragment: Fragment, isAnim: Boolean = true) {
        activity.supportFragmentManager.beginTransaction().apply {
            if (isAnim)
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    android.R.anim.fade_in,
                    android.R.anim.slide_out_right
                )
        }
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
    fun diaLogBottom(
        context: Context,
        layoutInflater: LayoutInflater,
    ): BottomSheetDialog {
        val sheetDialog = BottomSheetDialog(context, R.style.SheetDialog)
        val viewDialog: View = layoutInflater.inflate(R.layout.fragment_play_list, null)

        sheetDialog.setContentView(viewDialog)
        return sheetDialog
    }
    fun setProgresMotion(motionLayout: MotionLayout, progress: Float) {
        if (ViewCompat.isLaidOut(motionLayout)) {
            motionLayout.progress = progress
        } else {
            motionLayout.post { motionLayout.progress = progress }
        }
    }


    //Keyboard
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}