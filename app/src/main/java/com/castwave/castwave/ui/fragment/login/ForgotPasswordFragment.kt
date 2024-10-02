package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.databinding.FragmentForgotPasswordBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
    }

    private fun initAction() {
        binding.tvReceiveCode.setOnClickListener {
            showEnterOtpFragment()
        }
        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showEnterOtpFragment() {
        val gmail = binding.edtGmail.text.toString()
        if (isValidEmail(gmail)) {
            forgotPassword(gmail)
        } else {
            toast(requireContext().getString(R.string.txt_gmail_is_incorrect))
        }
    }

    private fun forgotPassword(gmail: String) {
        viewModel.rxForgotPassword.subscribe {
            Bundle().apply {
                putString(Constants.KEY_TYPE_OTP, Constants.KEY_FORGOT_PASSWORD)
                putString(Constants.KEY_SEND_OTP, gmail)
                openScreenWithContainer(
                    requireContext(),
                    EnterOtpFragment::class.java,
                    this
                )
            }
        }.addToBag()
        viewModel.rxMessage.subscribe { message ->
            toast(message)
        }.addToBag()
        viewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
        viewModel.forgotPassword(gmail)
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun getLayoutBinding(): FragmentForgotPasswordBinding =
        FragmentForgotPasswordBinding.inflate(layoutInflater)
}