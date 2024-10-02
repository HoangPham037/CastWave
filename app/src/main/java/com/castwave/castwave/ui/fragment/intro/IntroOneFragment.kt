package com.castwave.castwave.ui.fragment.intro

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Topic
import com.castwave.castwave.databinding.FragmentIntroOneBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroOneFragment : BaseFragment<FragmentIntroOneBinding>() {
    private val viewModel: MainViewModel by activityViewModels()

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        initAction()
    }

    private fun initData() {
        binding.rcvTopic.adapter = TopicAdapter(getListTopic())
        viewModel.updateProgress(Constants.INDEX_1)
    }

    private fun getListTopic(): List<Topic> {
        return arrayListOf(
            Topic(1, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(2, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(3, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(4, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(5, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(6, R.drawable.iv_topic, "Phát triển bản thân"),
            Topic(7, R.drawable.iv_topic, "Phát triển bản thân"),
        )
    }

    private fun initAction() {
        binding.tvContinue.setOnClickListener {
            openFragments(R.id.fraIntro, IntroTwoFragment::class.java, null, true)
        }
    }

    override fun onDestroyView() {
        viewModel.updateProgress(Constants.INDEX_0)
        super.onDestroyView()
    }

    override fun getLayoutBinding(): FragmentIntroOneBinding =
        FragmentIntroOneBinding.inflate(layoutInflater)

}