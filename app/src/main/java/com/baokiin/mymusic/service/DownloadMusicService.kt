package com.baokiin.mymusic.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
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
import java.io.File

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

        return START_STICKY
    }

    /**
     * download file mp3 from url
     * */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onDownloadMusicFileMp3(responseBody: EventBusModel.Response) {
        GlobalScope.launch(Dispatchers.IO) {
            idNoti++
            notification(0, responseBody.song)
            val path =
                getExternalFilesDir(null).toString() + File.separator.toString() + responseBody.song.songId + ".mp3"
            val isDownload = writeResponseBodyToDisk(responseBody.reponseBody, path) {
                map[idNoti - 1] = it
                notification(it, responseBody.song)
                EventBus.getDefault().post(EventBusModel.ProcessDownload(it))
            }
            if (isDownload) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this@DownloadMusicService.stopForeground(true)
                } else {
                    NotificationManagerCompat.from(this@DownloadMusicService).cancel(1234)
                }
                val songs = responseBody.song

                songs.apply {
                    val tmpSong = Song(
                        songId,
                        name,
                        code,
                        artistName,
                        responseBody.lyric,
                        responseBody.img,
                        thumb,
                        path,
                        isLiked
                    )
                    EventBus.getDefault().post(EventBusModel.DownloadMp3(tmpSong))
                }

            }

        }
    }

    private fun notification(pos: Int?, song: Song) {
        val notification = notificationBuilder().apply {
            setContentText(song.name + "  " + pos + "%")
            setProgress(100, pos ?: 0, false)
            //setGroup(GROUP_KEY)

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1234567, notification.build())
        } else {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1234567, notification.build())
            }
        }

    }

    private fun notificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, Utils.CHANNEL_DOWNLOAD)
            .setSmallIcon(R.drawable.ic_next)
            .setContentTitle("Downloading")
            .setContentText("Downloading")
            .setColor(1000)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
    }

}