package com.castwave.castwave.ui.fragment.media

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.databinding.CustomPopupDialogBinding
import com.castwave.castwave.databinding.FragmentPlayBinding
import com.castwave.castwave.service.MusicService
import com.castwave.castwave.utils.isMyServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation


@AndroidEntryPoint
class PlayFragment : BaseFragment<FragmentPlayBinding>() {
    private var service: MusicService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MusicService.MediaBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    override fun updateUI(savedInstanceState: Bundle?) {
        initViews()
        initAction()
        transformationsBackground()
        startService()
    }

    private fun initViews() {
        binding.tvName.isSelected = true
    }

    private fun transformationsBackground() {
        Glide.with(this).load(R.drawable.iv_cover)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(60, 3)))
            .into(binding.ivBackground)
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

    private fun initAction() {
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivMore.setOnClickListener { view ->
            openPopup(view)
        }
        binding.ivListChapter.setOnClickListener {

        }
    }

    private fun openPopup(view: View) {
        val popupDialog = BaseShowPopup(requireContext(), CustomPopupDialogBinding::inflate)
        popupDialog.binding.tvEvaluate.setOnClickListener {

        }
        popupDialog.binding.tvVipCard.setOnClickListener {

        }
        popupDialog.binding.tvInformation.setOnClickListener {

        }
        popupDialog.show(view, -500, 0, Gravity.BOTTOM)
    }

    override fun getLayoutBinding(): FragmentPlayBinding =
        FragmentPlayBinding.inflate(layoutInflater)
}