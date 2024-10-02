package com.castwave.castwave.ui.fragment.videoinfomentor

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentVideoInfoMentorBinding
import com.castwave.castwave.service.VideoService
import com.castwave.castwave.service.VideoService.Companion.CLOSE_EVENT
import com.castwave.castwave.service.VideoService.Companion.NEXT_EVENT
import com.castwave.castwave.service.VideoService.Companion.PLAY_EVENT
import com.castwave.castwave.service.VideoService.Companion.PREVIOUS_EVENT
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.onStopTrackingTouch
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.isMyServiceRunning
import com.castwave.castwave.utils.setupTime
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoInfoMentorFragment : BaseFragment<FragmentVideoInfoMentorBinding>() {
    private lateinit var player: ExoPlayer
    private var isReady = false
    private var isPlaying = true
    private var isToolOn = false
    private var service: VideoService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as VideoService.VideoBinder).getService()
            service?.setEvent { type ->
                listenerClick(type)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    private fun listenerClick(keyEvent: String?) {
        when (keyEvent) {
            PREVIOUS_EVENT -> {
                toast(PREVIOUS_EVENT)
            }

            PLAY_EVENT -> {
                playVideo()
            }

            NEXT_EVENT -> {
                toast(NEXT_EVENT)
            }

            CLOSE_EVENT -> {
                player.playWhenReady = false
                context?.apply {
                    unbindService(connection)
                    stopService(Intent(this , VideoService::class.java))
                }
            }
        }
    }

    companion object {
        private const val SEEK_TIME = 5000
        private const val TIME_TO_FADE = 500L
        private const val TIME_TO_HIDE_VIDEO_TOOL = 2000L
        private const val TIME_UPDATE_PROGRESS = 1000L
        private const val POSITION_MILLISECOND = 10L
        private const val SEEK_BAR_ALPHA = 255
        private const val PROGRESS_10 = 10
        private const val DURATION = 1000L
    }

    private val viewModel: AccountViewModel by viewModels()
    override fun getLayoutBinding(): FragmentVideoInfoMentorBinding =
        FragmentVideoInfoMentorBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initializePlayer()
        initAction()
    }

    private fun initAction() {
        onPlayClick()
        onPrevClick()
        onNextClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.layoutVideo.setOnTouchListener(View.OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> checkToolStatus()
            }
            return@OnTouchListener true
        })
        binding.layoutVideo.post {
            isToolOn = true
            fadeoutElements(TIME_TO_HIDE_VIDEO_TOOL)
        }
    }

    private fun checkToolStatus() {
        if (isToolOn) { // true an di
            isToolOn = false
            fadeoutElements(0L)
        } else { // false hien thi len
            showVideoTool()
            fadeoutElements(TIME_TO_HIDE_VIDEO_TOOL)
            isToolOn = true
        }
    }

    private fun onPlayClick() {
        binding.ivPlay.clickWithDebounce {
           playVideo()
        }
    }

    private fun playVideo() {
        if (isReady) {
            if (isPlaying) {
                player.playWhenReady = false
                Glide.with(requireContext()).load(R.drawable.ic_pause).into(binding.ivPlay)
                isPlaying = false
            } else {
                isPlaying = true
                player.playWhenReady = true
                startService()
                Glide.with(requireContext()).load(R.drawable.ic_play).into(binding.ivPlay)
            }
        }
        fadeoutElements(TIME_TO_HIDE_VIDEO_TOOL)
        service?.sendNotification(player)
    }

    private fun onPrevClick() {
        binding.ivPrevious.setOnClickListener {
            if (isReady) {
                if (player.currentPosition <= SEEK_TIME) {
                    player.seekTo(POSITION_MILLISECOND)
                    binding.seekbar.progress = PROGRESS_10
                    player.playWhenReady = isPlaying
                } else {
                    player.seekTo(player.currentPosition - SEEK_TIME)
                    binding.seekbar.progress = player.currentPosition.toInt()
                    player.playWhenReady = isPlaying
                }
            }
            fadeoutElements(TIME_TO_HIDE_VIDEO_TOOL)
        }
    }

    private fun onNextClick() {
        binding.ivNext.setOnClickListener {
            if (isReady) {
                if (player.currentPosition >= player.duration - SEEK_TIME) {
                    player.playWhenReady = false
                } else {
                    player.seekTo(player.currentPosition + SEEK_TIME)
                    binding.seekbar.progress = player.currentPosition.toInt()
                    player.playWhenReady = isPlaying
                }
            }
            fadeoutElements(TIME_TO_HIDE_VIDEO_TOOL)
        }
    }

    private fun initializePlayer() {
        showDialog()
        val dataSourceFactory = DefaultDataSource.Factory(requireContext())
        val mediaSourceFactory: MediaSource.Factory =
            DefaultMediaSourceFactory(dataSourceFactory).setAdViewProvider(binding.styledPlayerView)
        val builder = DefaultTrackSelector.ParametersBuilder(requireContext())
        val trackSelectorParameters = builder.build()
        val trackSelector = DefaultTrackSelector(requireContext())
        trackSelector.parameters = trackSelectorParameters
        player = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(mediaSourceFactory)
            .setTrackSelector(trackSelector)
            .build()
        binding.styledPlayerView.player = player
        player.playWhenReady = true
        val videoUri =
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        player.setMediaItem(MediaItem.fromUri(videoUri))
        player.prepare()
        (binding.styledPlayerView.player as ExoPlayer).addListener(object : Player.Listener {

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
                startService()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        isReady = false
                        showDialog()
                        service?.sendNotification(player)
                    }

                    Player.STATE_ENDED -> {
                        isReady = false
                        service?.sendNotification(player)
                    }

                    Player.STATE_IDLE -> isReady = false
                    Player.STATE_READY -> {
                        binding.seekbar.max = player.duration.toInt()
                        isReady = true
                        hideDialog()
                        service?.sendNotification(player)
                        println("--player.duration------------------------" + player.duration)
                    }

                    else -> isReady = false
                }
            }
        })

        binding.seekbar.onStopTrackingTouch { seekBar ->
            seekBar?.progress?.let {
                player.seekTo(it.toLong())
            }
        }
        viewModel.rxProgress.subscribe {
            binding.seekbar.progress = it.toInt()
            binding.tvTimeTotal.text = setupTime((player.duration - it - DURATION))
            binding.tvTimeStart.text = setupTime((it))
            service?.updateProgress(player)
        }.addToBag()
        viewModel.setProgress(TIME_UPDATE_PROGRESS, player)
    }

    private fun startService() {
        context?.apply {
            if (!isMyServiceRunning(this, VideoService::class.java)) {
                Intent(this, VideoService::class.java).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(this)
                    } else {
                        startService(this)
                    }
                    bindService(this, connection, BIND_AUTO_CREATE)
                }
            }
        }
    }

    private fun fadeView(
        v: View,
        isFadeIn: Boolean,
        delayTime: Long = 0L,
        time: Long = TIME_TO_FADE
    ) {
        v.clearAnimation()
        val fade = AlphaAnimation(if (isFadeIn) 0f else 1f, if (isFadeIn) 1f else 0f).apply {
            duration = time
            startOffset = delayTime
        }
        v.startAnimation(fade)
    }

    private fun showVideoTool() {
        binding.seekbar.thumb.mutate().alpha = SEEK_BAR_ALPHA
        fadeView(binding.layoutController, true)
        fadeView(binding.layoutSeekbar, true)
        binding.layoutSeekbar.show()
        binding.layoutController.show()
    }


    private fun fadeoutElements(delayTime: Long) {
        fadeView(binding.layoutController, false, delayTime)
        fadeView(binding.layoutSeekbar, false, delayTime)
        binding.layoutController.hide()
        binding.layoutSeekbar.hide()
        isToolOn = false
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        isPlaying = false
    }

    override fun onDestroy() {
        binding.layoutController.clearAnimation()
        binding.seekbar.clearAnimation()
        player.release()
        super.onDestroy()
    }

}