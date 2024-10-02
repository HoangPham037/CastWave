package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.databinding.FragmentResulResetPasswordBinding
import com.castwave.castwave.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultResetPasswordFragment : BaseFragment<FragmentResulResetPasswordBinding>() {

    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
        initData()
    }

    private fun initData() {
        arguments?.getString(Constants.KEY_TYPE_OTP)?.let { type ->
            if (type == Constants.KEY_SIGN_UP) {
                setupTextView(R.string.txt_new_account, R.string.txt_new_account_success)
            } else if (type == Constants.KEY_FORGOT_PASSWORD) {
                setupTextView(R.string.txt_password_change, R.string.txt_reset_password_success)
            }
        }
    }

    private fun setupTextView(title: Int, content: Int) {
        binding.tvTitle.text = requireContext().getString(title)
        binding.tvContent.text = requireContext().getString(content)
    }

    private fun initAction() {
        binding.tvSignIn.setOnClickListener {
            rxBus.send(
                Buser(
                    Constants.KEY_NAVIGATE_FRAGMENT,
                    Constants.VALUE_NAVIGATE_FRAGMENT
                )
            )
        }
    }

    override fun getLayoutBinding(): FragmentResulResetPasswordBinding =
        FragmentResulResetPasswordBinding.inflate(layoutInflater)
}