package com.castwave.castwave.ui.fragment.search.meditation

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Day
import com.castwave.castwave.databinding.FragmentMeditatioDetailBinding
import com.castwave.castwave.ui.adapter.DayAdapter
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeditationDetailFragment : BaseFragment<FragmentMeditatioDetailBinding>() {
    private val adapter by lazy { DayAdapter() }
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        setupAdapter()
        initAction()
    }

    private fun setupAdapter() {
        binding.rcvDay.adapter = adapter
        adapter.setEvent {
            openScreenWithContainer(requireContext(), PlayMeditationFragment::class.java, null)
        }
        adapter.items = getListDay()
    }

    private fun initView() {
        binding.tvTitleHeader.text = String.format("7 ngày rèn luyện sự tập trung")
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.linearLayout.setVisibility(!boolean)
            binding.tvTitleHeader.setVisibility(boolean)
        }
    }

    private fun getListDay(): MutableList<Day> {
        return arrayListOf(
            Day(1, "Ngày"),
            Day(2, "Ngày"),
            Day(3, "Ngày"),
            Day(4, "Ngày"),
            Day(5, "Ngày"),
            Day(6, "Ngày"),
            Day(7, "Ngày"),
            Day(8, "Ngày"),
            Day(9, "Ngày"),
            Day(10, "Ngày"),
            Day(11, "Ngày"),
            Day(12, "Ngày"),
            Day(13, "Ngày"),
            Day(14, "Ngày"),
        )
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun getLayoutBinding(): FragmentMeditatioDetailBinding =
        FragmentMeditatioDetailBinding.inflate(layoutInflater)
}