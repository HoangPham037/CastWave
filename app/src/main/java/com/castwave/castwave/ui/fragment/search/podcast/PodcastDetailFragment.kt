package com.castwave.castwave.ui.fragment.search.podcast

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentPodcastDetailBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodcastDetailFragment : BaseFragment<FragmentPodcastDetailBinding>() {

    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    private fun initView() {
        binding.tvTitleHeader.text = String.format("Lê Quốc Vinh")
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
        binding.tvPlay.setOnClickListener {
            openScreenWithContainer(requireContext(), PlayPodcastFragment::class.java, null)
        }
    }

    override fun getLayoutBinding() = FragmentPodcastDetailBinding.inflate(layoutInflater)
}