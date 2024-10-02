package com.castwave.castwave.ui.fragment.intro

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentIntroThreeBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroThreeFragment : BaseFragment<FragmentIntroThreeBinding>() {
    private val viewModel: MainViewModel by activityViewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
        viewModel.updateProgress(Constants.INDEX_3)
    }

    private fun initAction() {
        binding.tvContinue.setOnClickListener {
            openScreenWithContainer(requireContext(), ProgressFragment::class.java, null)
        }
    }


    override fun getLayoutBinding(): FragmentIntroThreeBinding {
        return FragmentIntroThreeBinding.inflate(layoutInflater)
    }

    override fun onDestroyView() {
        viewModel.updateProgress(Constants.INDEX_2)
        super.onDestroyView()
    }
}