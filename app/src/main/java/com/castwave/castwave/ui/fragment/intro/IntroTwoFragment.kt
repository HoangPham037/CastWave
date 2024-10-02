package com.castwave.castwave.ui.fragment.intro

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentIntroTwoBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroTwoFragment : BaseFragment<FragmentIntroTwoBinding>() {
    private val viewModel: MainViewModel by activityViewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
        viewModel.updateProgress(Constants.INDEX_2)
    }

    private fun initAction() {
        binding.tvLater.setOnClickListener {
            openNotification()
        }
        binding.btnOpenNotification.setOnClickListener {
            openNotification()
        }
    }

    private fun openNotification() {
        openFragments(R.id.fraIntro, IntroThreeFragment::class.java, null, true)
    }

    override fun getLayoutBinding(): FragmentIntroTwoBinding {
        return FragmentIntroTwoBinding.inflate(layoutInflater)
    }

    override fun onDestroyView() {
        viewModel.updateProgress(Constants.INDEX_1)
        super.onDestroyView()
    }
}