package com.castwave.castwave.ui.fragment.account

import android.graphics.Color
import android.os.Bundle
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Badge
import com.castwave.castwave.databinding.FragmentBadgeBinding
import com.castwave.castwave.ui.adapter.BadgeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeFragment : BaseFragment<FragmentBadgeBinding>() {
    private val adapter by lazy { BadgeAdapter() }

    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        setupAdapter()
        initAction()
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        activity?.setActionBar(binding.mainToolbar)
        binding.mainToolbar.title = "HUY HIỆU"
        binding.mainToolbar.setTitleTextColor(Color.WHITE)
    }

    private fun setupAdapter() {
        binding.rcvBadge.adapter = adapter
        adapter.items = getListBadge()
    }

    private fun getListBadge(): MutableList<Badge> {
        return arrayListOf(
            Badge(1, 2, 5, "Bronze cup", 1, R.drawable.ic_bronze_cup),
            Badge(1, 4, 15, "Silver cup", 1, R.drawable.ic_silver_cup),
            Badge(1, 12, 20, "Gold cup", 1, R.drawable.ic_gold_cup),
            Badge(1, 7, 14, "Diamond cup", 1, R.drawable.ic_diamind_cup),
            Badge(1, 8, 25, "Bronze cup", 1, R.drawable.ic_bronze_cup),
        )
    }

    override fun getLayoutBinding(): FragmentBadgeBinding =
        FragmentBadgeBinding.inflate(layoutInflater)
}