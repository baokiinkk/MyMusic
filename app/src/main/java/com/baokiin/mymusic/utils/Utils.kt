package com.baokiin.mymusic.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout


object Utils {
    const val TAG = "quocbao"

    const val ACTION_PLAY = 1
    const val ACTION_STOP = 2
    const val ACTION = "action"
    const val CHANNEL_ID = "123457"
    const val SONG = "Song"
    const val BITMAP = "Bitmap"


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
        val viewDialog: View = layoutInflater.inflate(R.layout.play_music, null)

        sheetDialog.setContentView(viewDialog)
        return sheetDialog
    }
}