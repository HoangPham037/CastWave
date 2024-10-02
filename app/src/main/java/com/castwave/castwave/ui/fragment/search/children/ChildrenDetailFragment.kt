package com.castwave.castwave.ui.fragment.search.children

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentChildrenDetailBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setDrawableEnd
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.MeditationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ChildrenDetailFragment : BaseFragment<FragmentChildrenDetailBinding>() {
    private var isSeeMore = false
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        updateTextViewSeeMore(isSeeMore)
    }

    private fun initView() {
        binding.tvTitleHeader.text = String.format("Người cung trăng - Những vệ thần tuổi thơ")
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.linearLayout.setVisibility(!boolean)
            binding.tvTitleHeader.setVisibility(boolean)
        }
    }

    private fun updateTextViewSeeMore(isSeeMore: Boolean) {
        binding.tvSeeMore.text = if (isSeeMore) resources.getString(
            R.string.txt_hide_see_more
        ) else resources.getString(R.string.txt_see_more)
        binding.tvContent.ellipsize = if (isSeeMore) null else TextUtils.TruncateAt.END
        binding.tvContent.maxLines =
            if (isSeeMore) Int.MAX_VALUE else Constants.COLLAPSE_LINE_CONTENT
        binding.tvSeeMore.setDrawableEnd(if (isSeeMore) R.drawable.ic_hide_see_more else R.drawable.ic_see_more)
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.tvSeeMore.setOnClickListener {
            isSeeMore = !isSeeMore
            updateTextViewSeeMore(isSeeMore)
        }
        binding.tvListening.setOnClickListener {
            openScreenWithContainer(requireContext(), PlayChildrenFragment::class.java, null)
        }
        binding.tvDownload.setOnClickListener {
            downloadFile()
        }
    }
    private val path =
        "https://videos.pexels.com/video-files/1390942/1390942-uhd_2732_1440_24fps.mp4"

    private fun downloadFile() {
        showDialog()
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/Download/Music")
        if (!folder.exists()) folder.mkdir()
        val filename = path.split("/").last()
        val downloadManager =
            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(path))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "Music/$filename"
        )
        val downloadId = downloadManager.enqueue(request)
        val downloadCompleteReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    hideDialog()
                    toast("download success")
                }
            }
        }
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(downloadCompleteReceiver, filter, RECEIVER_EXPORTED)
        } else {
            requireActivity().registerReceiver(downloadCompleteReceiver, filter)
        }
    }


    override fun getLayoutBinding(): FragmentChildrenDetailBinding =
        FragmentChildrenDetailBinding.inflate(layoutInflater)
}