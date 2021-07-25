package com.baokiin.mymusic.ui.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.baokiin.mymusic.R
import com.baokiin.mymusic.broadcast.MyBroadcastReceiver
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.utils.Utils.ACTION
import com.baokiin.mymusic.utils.Utils.ACTION_PLAY
import com.baokiin.mymusic.utils.Utils.ACTION_STOP
import com.baokiin.mymusic.utils.Utils.BITMAP
import com.baokiin.mymusic.utils.Utils.CHANNEL_ID
import com.baokiin.mymusic.utils.Utils.SONG
import com.baokiin.mymusic.utils.Utils.TAG


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
        bitmap?.let { bitmap ->
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
        val media = MediaSessionCompat(this, TAG)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_next)
            .setContentTitle(song.name)
            .setContentText(song.artists_names)
            .setLargeIcon(bitmap)
            .addAction(R.drawable.ic_prev, "Previous", null)
            .addAction(
                if (mediaPlayer?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play,
                "play",
                getPendingIntent(
                    this,
                    ACTION_PLAY
                )
            )
            .addAction(R.drawable.ic_next, "Next", null)
            .setStyle(
                MediaStyle()
                    .setShowActionsInCompactView(1)
                    .setMediaSession(media.sessionToken)
            )

        startForeground(101, notificationBuilder.build())
    }
}