package com.baokiin.mymusic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.app.NotificationCompat.MediaStyle
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.baokiin.mymusic.R
import com.baokiin.mymusic.broadcast.MyBroadcastReceiver
import com.baokiin.mymusic.data.model.EventBusModel.*
import com.baokiin.mymusic.data.model.Song
import com.baokiin.mymusic.utils.Utils.ACTION
import com.baokiin.mymusic.utils.Utils.ACTION_NEXT
import com.baokiin.mymusic.utils.Utils.ACTION_PLAY
import com.baokiin.mymusic.utils.Utils.ACTION_PREV
import com.baokiin.mymusic.utils.Utils.ACTION_STOP
import com.baokiin.mymusic.utils.Utils.CHANNEL_ID
import com.baokiin.mymusic.utils.Utils.SONG
import com.baokiin.mymusic.utils.Utils.TAG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MediaService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var msong: MutableList<Song>
    private var indexMedia = 0
    private var job: Job? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val song = intent?.getSerializableExtra(SONG) as Song?
        val action = intent?.getIntExtra(ACTION, -1)
        if (action != null && action != -1) {
            handleActionMusic(action)
        }
        song?.let {
            msong = mutableListOf(it)

            if (indexMedia > msong.size - 1 || indexMedia < 0)
                indexMedia = 0
            mediaPlayer?.stop()
            mediaPlayer = startMedia(msong[indexMedia])
            sendNotification(msong[indexMedia])
            mediaPlayer?.let { mediaPlayer ->
                timeSend(mediaPlayer)
                EventBus.getDefault().post(MediaInfo(msong[indexMedia], mediaPlayer))
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
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessageEvent(song: Songs) {
        song.index?.let {
            msong = mutableListOf()
            indexMedia = it
        }
        msong.addAll(song.song)

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMessagePosition(currentPosition: TimesLong) {
        if (mediaPlayer?.isPlaying == true)
            mediaPlayer?.start()
        mediaPlayer?.seekTo(currentPosition.time.toInt())
    }

    private fun timeSend(mediaPlayer: MediaPlayer) {
        if (job?.isActive == true)
            job?.cancel()
        job = GlobalScope.launch {
            while (true) {
                try {
                    if (mediaPlayer.currentPosition >= mediaPlayer.duration) {
                        nextMusic(true)
                        break
                    }
                    EventBus.getDefault().post(Times(mediaPlayer.currentPosition))
                    delay(500)
                } catch (e: Exception) {
                    break
                }
            }
        }
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            ACTION_PLAY -> {
                switchMusic()
            }
            ACTION_NEXT -> {
                nextMusic(true)
            }
            ACTION_PREV -> {
                nextMusic(false)
            }
            ACTION_STOP -> {
                stopSelf()
            }
        }
    }

    private fun nextMusic(boolean: Boolean) {
        if (boolean)
            indexMedia++
        else
            indexMedia--
        if (indexMedia > msong.size - 1 || indexMedia < 0)
            indexMedia = 0
        mediaPlayer?.stop()
        mediaPlayer = startMedia(msong[indexMedia])
        mediaPlayer?.let {
            sendNotification(msong[indexMedia])
            timeSend(it)
            EventBus.getDefault().post(MediaInfo(msong[indexMedia], it))
        }

    }

    private fun switchMusic() {
        mediaPlayer?.let {
            if (it.isPlaying)
                it.pause()
            else
                it.start()
            sendNotification(msong[indexMedia])
            EventBus.getDefault().post(MediaInfo(msong[indexMedia], it))
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
            setDataSource(song.song ?: "http://api.mp3.zing.vn/api/streaming/audio/${song.id}/320")
            prepare() //
            start()
        }
    }

    private fun sendNotification(song: Song) {
        GlobalScope.launch {
            val bitmap: Bitmap
            val loader = ImageLoader(this@MediaService)
            val request = ImageRequest.Builder(this@MediaService)
                .data(song.thumbnail)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            bitmap = (result as BitmapDrawable).bitmap


            mediaPlayer?.let { mediaPlayer ->
                val media = setUpMedia()

                val notificationBuilder =

                    NotificationCompat.Builder(this@MediaService, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_next)
                        .setContentTitle(song.name)
                        .setContentText(song.artists_names)
                        .setLargeIcon(bitmap)
                        .addAction(
                            R.drawable.ic_prev, "Previous", getPendingIntent(
                                this@MediaService,
                                ACTION_PREV
                            )
                        )
                        .addAction(
                            if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                            "play",
                            getPendingIntent(
                                this@MediaService,
                                ACTION_PLAY
                            )
                        )
                        .addAction(
                            R.drawable.ic_next, "Next", getPendingIntent(
                                this@MediaService,
                                ACTION_NEXT
                            )
                        )
                        .setStyle(
                            MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
                                .setMediaSession(media.sessionToken)
                        )
                        .setProgress(mediaPlayer.duration, mediaPlayer.currentPosition, false)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(123, notificationBuilder.build())
                } else {
                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(123, notificationBuilder.build())
                    }
                }
            }
        }
    }

    private fun setUpMedia(): MediaSessionCompat {
        return MediaSessionCompat(this, TAG).apply {
            setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
                    .build()
            )
        }

    }
}