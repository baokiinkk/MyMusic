package com.baokiin.mymusic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.baokiin.mymusic.R
import com.baokiin.mymusic.adapter.ItemSongLikeAdapter
import com.baokiin.mymusic.data.model.SongLike
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import java.io.*
import java.net.InetAddress
import java.util.*


object Utils {
    const val TAG = "quocbao"
    const val SNS_RESULT_CODE = 777
    const val SNS_REQUEST_CODE_GOOGLE = 887

    const val SNS_REQUEST_CODE_PHONE = 886

    const val ACTION_PLAY = 1
    const val ACTION_STOP = 2
    const val ACTION = "action"
    const val ACTION_NEXT = 4
    const val ACTION_PREV = 3
    const val CHANNEL_ID = "123456"
    const val CHANNEL_DOWNLOAD = "12345"
    const val SONG = "Song"
    const val VPOP = "vpop"
    const val KPOP = "kpop"
    const val USUK = "usuk"
    const val TREND = "trend"
    const val CATEGORY = "tagegory"
    const val GROUP_KEY = "com.baokiin.mymusic.download"
    const val LIKE = "like"
    const val DOWNLOAD = "download"
    const val STATUS_GETDATA = "getdata"
    const val STATUS_UPDATA = "updata"
    const val STATUS_LOGIN_OK = "login"


    fun gotoFragment(activity: FragmentActivity, fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.containerFramgnet, fragment)
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
            .add(R.id.containerFramgnet, fragment)
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

    //swipe recycleView
    fun callBackSwipe(
        context: View,
        adapter: ItemSongLikeAdapter,
        action: (MutableList<SongLike>) -> Unit
    ): ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val list = adapter.currentList
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                try {
                    Collections.swap(list, from, to)
                    adapter.submitList(list)
                    adapter.notifyItemMoved(from, to)
                }
                catch (e:Exception){

                }

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val listCurrent = mutableListOf<SongLike>()
                listCurrent.addAll(adapter.currentList)
                if (direction == ItemTouchHelper.LEFT) {
                    val positionDelete = viewHolder.adapterPosition
                    val song = listCurrent.removeAt(positionDelete)
                    adapter.submitList(listCurrent)
                    action(listCurrent)
                    Snackbar.make(context, "Đã xóa bài hát", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            val listUndo = mutableListOf<SongLike>()
                            listUndo.addAll(adapter.currentList)
                            listUndo.add(positionDelete, song)
                            adapter.submitList(listUndo)
                            action(listUndo)
                        }
                        .show()
                }
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
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun startServiceMusic(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else
            context.startService(intent)
    }

    fun writeResponseBodyToDisk(
        responseBody: ResponseBody,
        path: String,
        action: (Int) -> Unit
    ): Boolean {
        return try {
            val body = responseBody
            val futureStudioIconFile =
                File(path)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(7 * 1024)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    action(((fileSizeDownloaded / fileSize.toDouble()) * 100).toInt())
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    fun isInternetPing(context: Context): Boolean {
        return try {
            if(isInternetAvailable(context)){
                val ipAddr: InetAddress = InetAddress.getByName("www.google.com")
                !ipAddr.equals("")
            }
            else
                false

        } catch (e: Exception) {
            false
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val result:Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }

}