package com.castwave.castwave.ui.fragment.intro

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentIntroMainBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroMainFragment : BaseFragment<FragmentIntroMainBinding>() {
    private val viewModel: MainViewModel by activityViewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            setUpProgress(progress)
        }
        initView()
        initAction()
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpProgress(progress: Int) {
        binding.tvProgress.text = String.format("Bước $progress / ${Constants.INDEX_3}")
        binding.ivProgressOne.setImageLevel(if (progress >= Constants.INDEX_1) Constants.INDEX_1 else Constants.INDEX_0)
        binding.ivProgressTwo.setImageLevel(if (progress >= Constants.INDEX_2) Constants.INDEX_1 else Constants.INDEX_0)
        binding.ivProgressThree.setImageLevel(if (progress >= Constants.INDEX_3) Constants.INDEX_1 else Constants.INDEX_0)
        binding.viewOne.setBackgroundResource(if (progress >= Constants.INDEX_1) R.drawable.bg_view_true else R.drawable.bg_view_false)
        binding.viewTwo.setBackgroundResource(if (progress >= Constants.INDEX_2) R.drawable.bg_view_true else R.drawable.bg_view_false)
    }

    private fun initView() {
        openFragment(R.id.fraIntro, IntroOneFragment::class.java, null, true)
    }

    override fun getLayoutBinding(): FragmentIntroMainBinding =
        FragmentIntroMainBinding.inflate(layoutInflater)
}