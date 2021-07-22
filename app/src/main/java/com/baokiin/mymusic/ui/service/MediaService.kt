package com.baokiin.mymusic.ui.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import coil.load
import com.baokiin.mymusic.R
import com.baokiin.mymusic.broadcast.MyBroadcastReceiver
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.ui.activity.MainActivity
import com.baokiin.mymusic.utils.Utils.ACTION
import com.baokiin.mymusic.utils.Utils.ACTION_PLAY
import com.baokiin.mymusic.utils.Utils.ACTION_STOP
import com.baokiin.mymusic.utils.Utils.BITMAP
import com.baokiin.mymusic.utils.Utils.CHANNEL_ID
import com.baokiin.mymusic.utils.Utils.SONG


class MediaService : Service() {
    var mediaPlayer: MediaPlayer? = null
    private lateinit var msong: Song
    private lateinit var mbitmap: Bitmap
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val song = intent?.getSerializableExtra(SONG) as Song?
        val bitmap = intent?.getParcelableExtra(BITMAP) as Bitmap?
        val action = intent?.getIntExtra(ACTION, -1)
        if (action != null && action != -1) {
            handleActionMusic(action)
        }
        bitmap?.let { bitmap->
            mbitmap = bitmap
            song?.let {
                msong = it
                mediaPlayer?.stop()
                mediaPlayer = startMedia(it)
                sendNotification(it, bitmap)
            }
        }


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        if (mediaPlayer != null) {
            mediaPlayer = null
        }
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            ACTION_PLAY -> {
                switchMusic()
            }
            ACTION_STOP -> {
                stopSelf()
            }
        }
    }

    private fun switchMusic() {
        mediaPlayer?.let {
            if (it.isPlaying)
                it.pause()
            else
                it.start()
            sendNotification(msong, mbitmap)
        }
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        intent.putExtra(ACTION, action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startMedia(song: Song): MediaPlayer {
        return MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(song.song)
            prepare() //
            start()
        }
    }

    private fun sendNotification(song: Song, bitmap: Bitmap) {
        val action =
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        val v = ImageView(this)
        v.load(song.thumbnail)
//        val bm = (v.drawable as BitmapDrawable).bitmap
        val remoteView = RemoteViews(packageName, R.layout.layout_notification)
        remoteView.apply {
            setTextViewText(R.id.txtName, song.name)
            setTextViewText(R.id.txtTacGia, song.artists_names)
            setImageViewBitmap(R.id.imageView, bitmap)
            setImageViewResource(
                R.id.btnPlayPause,
                if (mediaPlayer?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
            setOnClickPendingIntent(
                R.id.btnPlayPause,
                getPendingIntent(this@MediaService, ACTION_PLAY)
            )
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel service media",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "demo"
            channel.setSound(null, null)
            channel.setShowBadge(false)
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        builder
            .setContentIntent(action)
            .setContentTitle("Demo")
            .setSmallIcon(R.drawable.ic_next)
            .setCustomBigContentView(remoteView)
        startForeground(101, builder.build())
    }
}