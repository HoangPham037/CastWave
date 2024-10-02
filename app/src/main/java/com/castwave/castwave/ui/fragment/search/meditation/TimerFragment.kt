package com.castwave.castwave.ui.fragment.search.meditation

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentTimerBinding
import com.castwave.castwave.media.MediaManager
import com.castwave.castwave.service.MusicService
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.isMyServiceRunning
import com.castwave.castwave.utils.setupTime
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TimerFragment : BaseFragment<FragmentTimerBinding>() {
    private var timeSelected: Long = 3000
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress: Long = 0
    private var pauseOffset: Long = 0
    private val path =
        "https://firebasestorage.googleapis.com/v0/b/social-media-9d614.appspot.com/o/audio%2F123.mp3?alt=media&token=30576889-70a2-4d3b-958d-a7b0ebd570ed"
    private var service: MusicService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MusicService.MediaBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    override fun updateUI(savedInstanceState: Bundle?) {
        showDialog()
        initViews()
        openSound()
        initAction()
    }

    private fun startService() {
        context?.apply {
            if (!isMyServiceRunning(this, MusicService::class.java)) {
                Intent(this, MusicService::class.java).apply {
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

    private fun openSound() {
        MediaManager.state.observe(viewLifecycleOwner) { state ->
            startService()
            startTimerSetup(state == Constants.INDEX_2)
        }
        MediaManager.getInstance()?.play(path)
    }

    private fun initAction() {
        binding.ivPlay.setOnClickListener {
            MediaManager.getInstance()?.play(path)
            service?.sendNotification()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initViews() {
        timeSelected = arguments?.getLong(Constants.KEY_TIMER) ?: 0
    }

    private fun startTimerSetup(isStart: Boolean) {
        binding.ivPlay.setImageLevel(if (isStart) Constants.INDEX_1 else Constants.INDEX_0)
        if (isStart) hideDialog()
        if (timeSelected > timeProgress) {
            if (isStart) startTimer(pauseOffset) else timeCountDown?.cancel()
        }
    }

    private fun startTimer(pauseOffSet: Long) {
        timeCountDown =
            object : CountDownTimer(timeSelected - pauseOffSet, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeProgress += 1000
                    pauseOffset = timeSelected - millisUntilFinished
                    binding.tvTimer.text = setupTime(timeSelected - timeProgress)
                }

                override fun onFinish() {
                    pauseOffset = 0
                    timeProgress = 0
                    binding.tvTimer.text = String.format("00:00")
                    timeSelected = arguments?.getLong(Constants.KEY_TIMER) ?: 0
                    MediaManager.getInstance()?.pause()
                }
            }
        timeCountDown?.start()
    }

    override fun onDestroyView() {
        timeCountDown?.cancel()
        timeCountDown = null
        MediaManager.getInstance()?.stop()
        context?.apply {
            if (isMyServiceRunning(this, MusicService::class.java)) {
                stopService(Intent(requireContext(), MusicService::class.java))
                unbindService(connection)
            }
        }
        super.onDestroyView()
    }

    override fun getLayoutBinding() = FragmentTimerBinding.inflate(layoutInflater)
}