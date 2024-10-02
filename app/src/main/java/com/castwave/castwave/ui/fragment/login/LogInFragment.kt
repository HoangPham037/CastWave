package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.databinding.FragmentLogInBinding
import com.castwave.castwave.ui.fragment.intro.IntroMainFragment
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>() {
    @Inject
    lateinit var preferences: Preferences
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
    }

    private fun initAction() {
        binding.tvSignIn.setOnClickListener {
            openScreenWithContainer(requireContext(), SignInFragment::class.java, null)
        }
        binding.tvSignUp.setOnClickListener {
            openScreenWithContainer(requireContext(), SignUpFragment::class.java, null)
        }
        binding.tvContinueGuest.setOnClickListener {
            openScreenWithContainer(requireContext(), IntroMainFragment::class.java, null)
        }
    }

    override fun getLayoutBinding(): FragmentLogInBinding =
        FragmentLogInBinding.inflate(layoutInflater)
}