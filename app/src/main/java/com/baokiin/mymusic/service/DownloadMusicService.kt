package com.baokiin.mymusic.service

import android.app.Service
import android.content.Intent
import android.os.Build

import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.baokiin.mymusic.R
import com.baokiin.mymusic.data.model.EventBusModel
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.utils.Utils
import com.baokiin.mymusic.utils.Utils.GROUP_KEY
import com.baokiin.mymusic.utils.Utils.writeResponseBodyToDisk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*

class DownloadMusicService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private var idNoti = 0
    private var map: HashMap<Int, Int> = hashMapOf()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = notificationBuilder().apply {
            setGroup(GROUP_KEY)
            setGroupSummary(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1234, notification.build())
        }
        else{
            with(NotificationManagerCompat.from(applicationContext)){
                notify(1234, notification.build())
            }
        }
        return START_STICKY
    }

    /**
     * download file mp3 from url
     * */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onDownloadMusicFileMp3(responseBody: EventBusModel.Reponse) {
        GlobalScope.launch(Dispatchers.IO) {
            idNoti++
            val path = getExternalFilesDir(null).toString() + File.separator.toString() +responseBody.song.id+ ".mp3"
            val isDownload= writeResponseBodyToDisk(responseBody.reponseBody,path){
                map[idNoti - 1] = it
                notification(idNoti,responseBody.song)
            }
            if(isDownload){
                stopForeground(true)
                val song = responseBody.song
                song.song = path
                EventBus.getDefault().post(EventBusModel.DownloadMp3(song))
            }
        }
    }

    private fun notification(id: Int,song: Song) {
        val notification = notificationBuilder().apply {
            setContentText(song.name + "  " + map[id - 1] + "%")
            setProgress(100, map[id - 1] ?: 0, false)
            setGroup(GROUP_KEY)
        }
        startForeground(id, notification.build())

    }
    private fun notificationBuilder():NotificationCompat.Builder{
        return NotificationCompat.Builder(this, Utils.CHANNEL_DOWNLOAD)
            .setSmallIcon(R.drawable.ic_next)
            .setContentTitle("Downloading")
    }

}