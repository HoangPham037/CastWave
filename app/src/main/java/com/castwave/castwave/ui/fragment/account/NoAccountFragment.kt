package com.castwave.castwave.ui.fragment.account

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.databinding.FragmentNoAccountBinding
import com.castwave.castwave.ui.fragment.login.LogInFragment
import com.castwave.castwave.ui.fragment.login.SignUpFragment
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoAccountFragment : BaseFragment<FragmentNoAccountBinding>() {
    @Inject
    lateinit var preferences: Preferences
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvLogIn.setOnClickListener {
            openScreenWithContainer(requireContext(), LogInFragment::class.java, null)
        }
        binding.tvSignUp.setOnClickListener {
            openScreenWithContainer(requireContext(), SignUpFragment::class.java, null)
        }
    }

    override fun getLayoutBinding(): FragmentNoAccountBinding =
        FragmentNoAccountBinding.inflate(layoutInflater)
}