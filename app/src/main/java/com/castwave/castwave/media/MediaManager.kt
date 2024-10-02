package com.castwave.castwave.media

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.castwave.castwave.utils.Constants
import java.io.IOException

open class MediaManager {
    val STATE_IDLE: Int = 1
    val STATE_PLAYING: Int = 2
    val STATE_PAUSED: Int = 3
    var statePlay = STATE_IDLE
    companion object {
        var state: MutableLiveData<Int> = MutableLiveData()
         var player: MediaPlayer? = null
        private var instance: MediaManager? = null
        fun getInstance(): MediaManager? {
            if (instance == null) {
                instance = MediaManager()
                player?.setOnCompletionListener {
                    state.postValue(Constants.INDEX_1)
                }
            }
            return instance
        }
    }

    init {
        player = MediaPlayer()
    }


    fun play(path: String) {
        when (statePlay) {
            STATE_IDLE -> {
                player?.stop()
                player?.reset()
                try {
                    player?.setDataSource("https://firebasestorage.googleapis.com/v0/b/social-media-9d614.appspot.com/o/audio%2F123.mp3?alt=media&token=30576889-70a2-4d3b-958d-a7b0ebd570ed")
                    player?.prepareAsync()
                    player?.setOnPreparedListener {
                        it.start()
                        statePlay = STATE_PLAYING
                        state.postValue(STATE_PLAYING)
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                } catch (p: IllegalStateException) {
                    p.printStackTrace()
                }
            }

            STATE_PLAYING -> {
                player?.pause()
                statePlay = STATE_PAUSED
                state.postValue(STATE_PAUSED)
            }

            STATE_PAUSED -> {
                player?.start()
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
                player?.start()
                statePlay = STATE_PLAYING
                state.postValue(STATE_PLAYING)
            }
        }
    }
    fun setSpeedAudio(playbackSpeed: Float) {
        player?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) mediaPlayer.playbackParams.setSpeed(playbackSpeed)
                ?.let { mediaPlayer.playbackParams = it }
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
            media.reset()
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

    fun getCurrent(): Int {
        return try {
            player?.currentPosition ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun getTotalTime(): Int {
        return try {
            player?.duration ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun seekTo(progress: Int?) {
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