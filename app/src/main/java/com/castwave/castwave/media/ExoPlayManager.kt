package com.castwave.castwave.media

import androidx.lifecycle.MutableLiveData
import com.castwave.castwave.di.hitlcontainer.BaseApp
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import java.io.IOException

open class ExoPlayManager {
    val STATE_IDLE: Int = 1
    val STATE_PLAYING: Int = 2
    val STATE_PAUSED: Int = 3
    var statePlay = STATE_IDLE
    var player: ExoPlayer? = null

    companion object {
        var state: MutableLiveData<Int> = MutableLiveData()
        private var instance: ExoPlayManager? = null
        fun getInstance(): ExoPlayManager? {
            if (instance == null) {
                instance = ExoPlayManager()
            }
            return instance
        }
    }

    fun initializeExoPlayer(
        mediaSourceFactory: MediaSource.Factory?,
        trackSelector: DefaultTrackSelector
    ) {
        player = mediaSourceFactory?.let {
            ExoPlayer.Builder(BaseApp.instance.applicationContext)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(trackSelector)
                .build()
        } ?: run {
            ExoPlayer.Builder(BaseApp.instance.applicationContext)
                .build()
        }
        player?.addListener(object : Player.Listener {
            override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
                println("------onTracksChanged-------------------")
            }

            override fun onPlayerError(error: PlaybackException) {
                println("------onPlayerError-------------------")
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                println("------onLoadingChanged-------------------")
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                println("------onPositionDiscontinuity-------------------$reason")
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                println("------onRepeatModeChanged-------------------$repeatMode")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                println("------onShuffleModeEnabledChanged-------------------$shuffleModeEnabled")
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                println("------onTimelineChanged-------------------$timeline")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {}
                    Player.STATE_ENDED -> {}
                    Player.STATE_IDLE -> {}
                    Player.STATE_READY -> {}
                }
            }
        })
    }


    fun play(path: String) {
        when (statePlay) {
            Player.STATE_IDLE -> {
                player?.stop()
                try {
                    player?.setMediaItem(MediaItem.fromUri(path))
                    player?.playWhenReady = true
                    player?.prepare()
                    statePlay = STATE_PLAYING
                    state.postValue(STATE_PLAYING)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                } catch (p: IllegalStateException) {
                    p.printStackTrace()
                }
            }

            STATE_PLAYING -> {
                player?.playWhenReady = false
                player?.pause()
                statePlay = STATE_PAUSED
                state.postValue(STATE_PAUSED)
            }

            STATE_PAUSED -> {
                player?.play()
                player?.playWhenReady = true
                statePlay = STATE_PLAYING
                state.postValue(STATE_PLAYING)
            }
        }
    }

    fun play() {
        when (statePlay) {
            STATE_PLAYING -> {
                player?.pause()
                statePlay = STATE_PAUSED
                state.postValue(STATE_PAUSED)
            }

            STATE_PAUSED -> {
                player?.play()
                statePlay = STATE_PLAYING
                state.postValue(STATE_PLAYING)
            }
        }
    }


    fun setSeekTo() {
        player?.let {
            it.seekTo(it.currentPosition - 5000)
        }
    }

    fun stop() {
        player?.let { media ->
            media.stop()
            media.release()
            statePlay = STATE_IDLE
            state.postValue(STATE_IDLE)
        }
    }

    fun pause() {
        player?.let { media ->
            if (media.isPlaying) {
                media.pause()
                statePlay = STATE_PAUSED
                state.postValue(STATE_PAUSED)
            }
        }
    }

    fun getCurrent(): Long {
        return try {
            player?.currentPosition ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun getTotalTime(): Long {
        return try {
            player?.duration ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun seekTo(progress: Long?) {
        progress?.let {
            try {
                player?.seekTo(progress)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun backSong() {

    }

    fun nextSong() {

    }
}