package com.castwave.castwave.ui.fragment.search.meditation

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentPlayMeditationBinding
import com.castwave.castwave.media.MediaManager
import com.castwave.castwave.service.MusicService
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.onStopTrackingTouch
import com.castwave.castwave.utils.isMyServiceRunning
import com.castwave.castwave.utils.setupTimeProgress
import com.castwave.castwave.viewmodel.MeditationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayMeditationFragment : BaseFragment<FragmentPlayMeditationBinding>() {
    private val viewModel: MeditationViewModel by viewModels()
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
        openSound()
        initView()
        initAction()
        startService()
    }

    private fun initView() {

    }

    private fun openSound() {
        MediaManager.state.observe(viewLifecycleOwner) { state ->
            if (state == Constants.INDEX_2) {
                hideDialog()
                updateTextTime()
                startService()
                binding.ivPlay.setImageLevel(Constants.INDEX_1)
            } else {
                binding.ivPlay.setImageLevel(Constants.INDEX_0)
            }
        }
        MediaManager.getInstance()?.play(path)
    }

    private fun updateTextTime() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    Thread.sleep(1000)
                    MediaManager.getInstance()?.getCurrent()?.let { currentTime ->
                        MediaManager.getInstance()?.getTotalTime()?.let { totalTime ->
                            launch(Dispatchers.Main) {
                                binding.seekbar.max = totalTime
                                binding.seekbar.progress = currentTime
                                service?.updateProgress(currentTime, totalTime)
                                binding.tvTimeTotal.text = setupTimeProgress(totalTime.toLong())
                                binding.tvCurrentTime.text = setupTimeProgress(currentTime.toLong())
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivPlay.setOnClickListener {
            MediaManager.getInstance()?.play(path)
            service?.sendNotification()
        }
        binding.seekbar.onStopTrackingTouch { seekBar ->
            seekBar?.let {
                MediaManager.getInstance()?.seekTo(seekBar.progress)
            }
        }
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

    override fun onDestroyView() {
        context?.apply {
            if (isMyServiceRunning(this, MusicService::class.java)) {
                stopService(Intent(requireContext(), MusicService::class.java))
                unbindService(connection)
            }
        }
        MediaManager.getInstance()?.stop()
        super.onDestroyView()
    }

    override fun getLayoutBinding() =
        FragmentPlayMeditationBinding.inflate(layoutInflater)
}