package com.castwave.castwave.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.app.NotificationCompat.MediaStyle
import com.castwave.castwave.R
import com.castwave.castwave.data.model.Song
import com.castwave.castwave.media.MediaManager
import com.castwave.castwave.ui.activity.ContainActivity
import com.castwave.castwave.utils.Constants
import com.google.android.exoplayer2.ExoPlayer

class VideoService : Service() {
    val binder = VideoBinder()
    var listMusic: List<Song> = arrayListOf()
    private lateinit var notification: NotificationCompat.Builder
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManager
    private var event: ((String?) -> Unit)? = null

    inner class VideoBinder : Binder() {
        fun getService() = this@VideoService
        fun setMusicList(list: List<Song>) {
            this@VideoService.listMusic = list.toMutableList()
        }
    }

    companion object {
        const val CHANNEL_ID = "channel_id_video"
        const val CHANNEL_NAME = "channel_name_video"
        const val PLAY_EVENT = "PLAY_EVENT"
        const val CLOSE_EVENT = "CLOSE_EVENT"
        const val NEXT_EVENT = "NEXT_EVENT"
        const val PREVIOUS_EVENT = "PREVIOUS_EVENT"
        const val KEY_EVENT = "KEY_EVENT"
    }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(baseContext, "My Video")
        createNotificationChannel()
        sendNotification(null)
    }

    private fun createNotificationChannel() {
        val description = "Enjoy video :))"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance)
            channel.description = description
            channel.setSound(null, null)
            notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun sendNotification(player: ExoPlayer?) {
        val intent = Intent(applicationContext, ContainActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_TASK_ON_HOME or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                Constants.INDEX_0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or FLAG_MUTABLE
            )
        val actionPlay: NotificationCompat.Action = NotificationCompat.Action(
            if (player?.isPlaying == true) R.drawable.ic_play_24dp else R.drawable.ic_pause_24dp,
            if (player?.isPlaying == true) "Play" else "Pause",
            getPendingIntent(PLAY_EVENT)
        )
        val actionPrevious: NotificationCompat.Action = NotificationCompat.Action(
            R.drawable.ic_previous,
            "Back",
            getPendingIntent(PREVIOUS_EVENT)
        )
        val actionNext: NotificationCompat.Action = NotificationCompat.Action(
            R.drawable.ic_next_16dp,
            "Next",
            getPendingIntent(NEXT_EVENT)
        )
        val actionClose: NotificationCompat.Action = NotificationCompat.Action(
            R.drawable.ic_close_24dp,
            "Close",
            getPendingIntent(CLOSE_EVENT)
        )
        val notificationStyle = MediaStyle()
            .setShowActionsInCompactView(Constants.INDEX_0, Constants.INDEX_1, Constants.INDEX_2)
            .setMediaSession(mediaSession.sessionToken)
        val bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.iv_cover)
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play_24dp)
            .setContentText("Video")
            .setContentTitle("Service Video")
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(actionPrevious)
            .addAction(actionPlay)
            .addAction(actionNext)
            .addAction(actionClose)
            .setLargeIcon(bitmap)
            .setShowWhen(false)
            .setAutoCancel(true)
            .setStyle(notificationStyle)
            .setProgress(100, 0, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        player?.duration ?: 0
                    )
                    .build()
            )
            mediaSession.setCallback(object : MediaSessionCompat.Callback() {
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    MediaManager.getInstance()?.seekTo(pos.toInt())
                    mediaSession.setPlaybackState(getPlayBackState(player))
                }
            })
        }
        mediaSession.setPlaybackState(getPlayBackState(player))
        startForeground(101, notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            event?.invoke(intent.action)
//            if (intent.action == CLOSE_EVENT) {
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
//                notificationManager.cancel(1001)
//                stopSelf()
//            }
//
//            when (intent.action) {
//                PREVIOUS_EVENT -> {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                }
//
//                PLAY_EVENT -> {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                }
//
//                NEXT_EVENT -> {
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//                }
//
//                CLOSE_EVENT -> {
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
//            notificationManager.cancel(1001)
//            stopSelf()
//                }
//
//                else -> {
//                    sendBroadcast(intent)
//                }
//            }
        }
        return START_STICKY
    }

    private fun getPlayBackState(player: ExoPlayer?): PlaybackStateCompat {
        val playbackSpeed = if (player?.isPlaying == true) 1F else 0F
        return PlaybackStateCompat.Builder().setState(
            if (player?.isPlaying == true) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
            player?.currentPosition ?: 0, playbackSpeed
        )
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            .build()
    }


    private fun getPendingIntent(type: String): PendingIntent? {
        val intent = Intent(applicationContext, VideoService::class.java).apply {
            action = type
        }
        return PendingIntent.getService(
            this.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    }

    fun updateProgress(player: ExoPlayer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) mediaSession.setPlaybackState(
            getPlayBackState(player)
        )
        notification.setProgress(player.duration.toInt(), player.currentPosition.toInt(), false)
    }

    fun setEvent(function: (String?) -> Unit) {
        event = function
    }
}

interface ListenerDuration {
    fun event(keyEvent: String?)
}