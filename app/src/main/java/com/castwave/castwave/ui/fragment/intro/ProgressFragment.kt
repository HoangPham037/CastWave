package com.castwave.castwave.ui.fragment.intro

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.databinding.FragmentProgressBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.Constants.INDEX_1
import com.castwave.castwave.viewmodel.ProgressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProgressFragment : BaseFragment<FragmentProgressBinding>() {
    private val viewModel: ProgressViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        binding.progressBar.apply {
            setProgressWidth(25)
            setProgressColor(requireContext().getColor(R.color.purple_01))
        }
        viewModel.viewModelScope.launch {
            for (i in INDEX_1..100) {
                delay(50)
                binding.progressBar.setProgress(i.toFloat())
            }
            delay(50)
            showDiscoveryFragment()
        }
    }

    private fun showDiscoveryFragment() {
        rxBus.send(Buser(Constants.KEY_NAVIGATE_FRAGMENT, Constants.KEY_NAVIGATE_FRAGMENT))
    }

    override fun getLayoutBinding(): FragmentProgressBinding =
        FragmentProgressBinding.inflate(layoutInflater)
}