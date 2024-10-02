package com.castwave.castwave.ui.fragment.search.podcast

import android.os.Bundle
import android.text.TextUtils
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.ChapterPodcast
import com.castwave.castwave.databinding.FragmentPodcastChannelBinding
import com.castwave.castwave.ui.adapter.ChannelAdapter
import com.castwave.castwave.ui.adapter.ChapterPodcastAdapter
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setDrawableEnd
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class PodcastChannelFragment : BaseFragment<FragmentPodcastChannelBinding>() {
    private val adapterPodcast by lazy { ChapterPodcastAdapter() }
    private val adapterChannel by lazy { ChannelAdapter() }
    private var isSeeMore = false
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        initAdapter()
        updateTextViewSeeMore(isSeeMore)
    }

    private fun initAdapter() {
        val list = getListPodcast()
        binding.rcvPodCast.adapter = adapterPodcast
        binding.rcvChannel.adapter = adapterChannel
        adapterPodcast.setEvent{
            openScreenWithContainer(requireContext() , PodcastDetailFragment::class.java , null)
        }
        adapterChannel.items = list
        adapterPodcast.items = if (list.size > Constants.INDEX_3) list.subList(
            Constants.INDEX_0,
            Constants.INDEX_3
        ) else list
        binding.tvShowAllPodcastOne.text = String.format("Tập Podcast (${list.size})")
        binding.tvShowAllPodcastTwo.text = String.format("Xem tất cả ${list.size} tập")
        binding.tvShowAllPodcastTwo.setVisibility(list.size > Constants.INDEX_3)
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

    private fun initView() {
        binding.tvTitleHeader.text = String.format("Little Prince")
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.linearLayout.setVisibility(!boolean)
            binding.ivAddTitleHeader.setVisibility(boolean)
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
        binding.tvAdd.setOnClickListener {
            showFragmentPodcastEpisode()
        }
        binding.tvShowAllPodcastOne.setOnClickListener {
            showFragmentPodcastEpisode()
        }
        binding.tvShowAllPodcastTwo.setOnClickListener {
            openScreenWithContainer(requireContext(), PodcastEpisodeFragment::class.java, null)
        }
    }

    private fun showFragmentPodcastEpisode() {
        openScreenWithContainer(requireContext(), PodcastEpisodeFragment::class.java, null)
    }

    override fun getLayoutBinding() = FragmentPodcastChannelBinding.inflate(layoutInflater)
}