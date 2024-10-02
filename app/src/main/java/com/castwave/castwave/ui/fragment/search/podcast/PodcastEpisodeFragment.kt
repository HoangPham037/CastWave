package com.castwave.castwave.ui.fragment.search.podcast

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.data.model.ChapterPodcast
import com.castwave.castwave.databinding.FragmentPodcastEpisodeBinding
import com.castwave.castwave.databinding.PopupDialogArrangeBinding
import com.castwave.castwave.databinding.PopupPodcastEpisodeBinding
import com.castwave.castwave.ui.adapter.ChapterPodcastAdapter
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.setVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodcastEpisodeFragment : BaseFragment<FragmentPodcastEpisodeBinding>() {
    private val adapterPodcastEpisode by lazy { ChapterPodcastAdapter() }
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        setupAdapter()
    }

    private fun initView() {
        binding.tvTitleHeader.text = String.format("Cosmic Writer")
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.linearLayout.setVisibility(!boolean)
            binding.tvTitleHeader.setVisibility(boolean)
        }
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.ivArrange.setOnClickListener { view ->
            openPopupArrange(view)
        }
        binding.tvAllPodcast.setOnClickListener { view ->
            openPopupShowPodcast(view)
        }
    }

    private fun openPopupShowPodcast(view: View) {
        val popupDialog = BaseShowPopup(requireContext(), PopupPodcastEpisodeBinding::inflate)
        popupDialog.show(view, 200, -50, Gravity.BOTTOM)
        popupDialog.binding.tvAllPodcast.setOnClickListener {
            popupDialog.dismiss()
            adapterPodcastEpisode.notifyDataSetChanged()
        }
        popupDialog.binding.tvDownloadedEpisode.setOnClickListener {
            popupDialog.dismiss()
            adapterPodcastEpisode.notifyDataSetChanged()
        }
    }

    private fun openPopupArrange(view: View) {
        val popupDialog = BaseShowPopup(requireContext(), PopupDialogArrangeBinding::inflate)
        popupDialog.binding.tvPopular.hide()
        popupDialog.show(view, -200, -50, Gravity.BOTTOM)
        popupDialog.binding.tvInformation.setOnClickListener {
            popupDialog.dismiss()
            adapterPodcastEpisode.notifyDataSetChanged()
        }
        popupDialog.binding.tvOldest.setOnClickListener {
            popupDialog.dismiss()
            adapterPodcastEpisode.notifyDataSetChanged()
        }
    }

    private fun setupAdapter() {
        binding.rcvPodcastEpisode.adapter = adapterPodcastEpisode
        adapterPodcastEpisode.items = getListPodcast()
        binding.tvAllPodcast.text = String.format("Tất cả các tập (${adapterPodcastEpisode.itemCountZ})")
    }

    private fun getListPodcast(): MutableList<ChapterPodcast> {
        return arrayListOf(
            ChapterPodcast(
                1,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://bold.vn/wp-content/uploads/2019/05/bold-academy-5.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                2,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://media-cdn-v2.laodong.vn/storage/newsportal/2023/8/26/1233821/Giai-Nhi-1--Nang-Tre.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                3,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/anh-dep-thien-nhien-2-1.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                4,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://images2.thanhnien.vn/528068263637045248/2024/1/25/e093e9cfc9027d6a142358d24d2ee350-65a11ac2af785880-17061562929701875684912.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                5,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/hinh-thien-nhien-3d-002.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                6,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/hinh-dep.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                7,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://internetviettel.vn/wp-content/uploads/2017/05/H%C3%ACnh-%E1%BA%A3nh-minh-h%E1%BB%8Da.jpg",
                "15 phút"
            ),
            ChapterPodcast(
                8,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://gcs.tripi.vn/public-tripi/tripi-feed/img/474113qfT/hinh-anh-tu-nhien-dep-binh-di_014859861.jpeg",
                "15 phút"
            ),
            ChapterPodcast(
                9,
                "Vượt qua nỗi sợ đánh giá",
                "01 tháng 8",
                "https://img.pikbest.com/ai/illus_our/20230418/64e0e89c52dec903ce07bb1821b4bcc8.jpg!w700wp",
                "15 phút"
            ),
        )
    }

    override fun getLayoutBinding(): FragmentPodcastEpisodeBinding =
        FragmentPodcastEpisodeBinding.inflate(layoutInflater)
}