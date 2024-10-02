package com.castwave.castwave.ui.fragment.search.meditation

import android.os.Bundle
import android.util.Log
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.BookTopic
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.databinding.FragmentMeditationCategoryBinding
import com.castwave.castwave.databinding.FragmentMeditationSeriesBinding
import com.castwave.castwave.ui.adapter.CategoryAudiobookAdapter
import com.castwave.castwave.ui.adapter.MeditationAdapter
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationCategoryFragment : BaseFragment<FragmentMeditationCategoryBinding>() {
    private val adapterMeditation by lazy { MeditationAdapter() }
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAdapter()
        initAction()
    }

    private fun initView() {
        activity?.setActionBar(binding.mainToolbar)
        activity?.actionBar?.title = "Làm quen với thiền"
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.tvName.setVisibility(!boolean)
        }
    }

    private fun initAdapter() {
        binding.rcvMeditation.adapter = adapterMeditation
        adapterMeditation.setEvent {
            openScreenWithContainer(requireContext() , MeditationDetailFragment::class.java , null)
        }
        adapterMeditation.items = getListBookTopic()
    }
    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getListBookTopic(): ArrayList<BookTopic> {
        return arrayListOf(
            BookTopic(1, "MIỄN PHÍ", "https://bold.vn/wp-content/uploads/2019/05/bold-academy-5.jpg", "25/08/2024"),
            BookTopic(2, "1 THẺ CASTWAVE", "https://media-cdn-v2.laodong.vn/storage/newsportal/2023/8/26/1233821/Giai-Nhi-1--Nang-Tre.jpg", "26/08/2024"),
            BookTopic(3, "MIỄN PHÍ", "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/anh-dep-thien-nhien-2-1.jpg", "27/08/2024"),
            BookTopic(4, "MIỄN PHÍ", "https://images2.thanhnien.vn/528068263637045248/2024/1/25/e093e9cfc9027d6a142358d24d2ee350-65a11ac2af785880-17061562929701875684912.jpg", "28/08/2024"),
            BookTopic(5, "1 THẺ CASTWAVE","https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/hinh-thien-nhien-3d-002.jpg", "29/08/2024"),
            BookTopic(6, "MIỄN PHÍ", "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/hinh-dep.jpg", "20/08/2024"),
            BookTopic(7, "MIỄN PHÍ", "https://internetviettel.vn/wp-content/uploads/2017/05/H%C3%ACnh-%E1%BA%A3nh-minh-h%E1%BB%8Da.jpg", "21/08/2024"),
            BookTopic(8, "1 THẺ CASTWAVE", "https://gcs.tripi.vn/public-tripi/tripi-feed/img/474113qfT/hinh-anh-tu-nhien-dep-binh-di_014859861.jpeg", "22/08/2024"),
            BookTopic(9, "MIỄN PHÍ", "https://img.pikbest.com/ai/illus_our/20230418/64e0e89c52dec903ce07bb1821b4bcc8.jpg!w700wp", "23/08/2024"),
            BookTopic(10, "1 THẺ CASTWAVE", "https://img.pikbest.com/ai/illus_our/20230418/64e0e89c52dec903ce07bb1821b4bcc8.jpg!w700wp", "24/08/2024"),
        )
    }

    override fun getLayoutBinding() = FragmentMeditationCategoryBinding.inflate(layoutInflater)
}