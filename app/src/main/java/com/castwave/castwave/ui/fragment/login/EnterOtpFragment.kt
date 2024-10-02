package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.customview.otp.OtpListener
import com.castwave.castwave.databinding.FragmentEnterOtpBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterOtpFragment : BaseFragment<FragmentEnterOtpBinding>() {
    private val accountViewModel: AccountViewModel by viewModels()
    private var secondsRemaining = 60
    private var otpResults: String? = null
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        startCountdown()
        binding.tvTime.isEnabled = false
    }

    private fun initView() {
        arguments?.getString(Constants.KEY_TYPE_OTP)?.let { type ->
            binding.tvResetOtp.text =
                requireContext().getString(if (type == Constants.KEY_SIGN_UP) R.string.txt_reset_otp else R.string.txt_code_expired)
        }
    }

    private fun initAction() {
        binding.ivClose.setOnClickListener { onBackPressed() }
        binding.tvConfirm.setOnClickListener {
            nextFragment()
        }
        binding.tvTime.setOnClickListener {
            binding.tvTime.isEnabled = false
            startCountdown()
            arguments?.getString(Constants.KEY_SEND_OTP)
                ?.let { it1 -> accountViewModel.sendOtp(it1) }
        }
        binding.otpView.requestFocusOTP()
        binding.otpView.otpListener = object : OtpListener {
            override fun onOtpFinish(otpResult: String) {
                otpResults = otpResult
            }
        }
    }

    private fun startCountdown() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.tvTime.text = secondsRemaining.toString()
            }

            override fun onFinish() {
                binding.tvTime.text = requireContext().getString(R.string.txt_resend)
                binding.tvTime.isEnabled = true
            }
        }.start()
    }

    private fun nextFragment() {
        val email = arguments?.getString(Constants.KEY_SEND_OTP)
        if (email != null) {
            if (otpResults == null) {
                binding.otpView.showErrorState()
                toast("Otp chưa đúng vui lòng thử lại !!")
            } else
                accountViewModel.verifyOtp(email, otpResults!!)
        }
        arguments?.getString(Constants.KEY_TYPE_OTP)?.let { type ->
            accountViewModel.rxVerifyOtp.subscribe {
                Bundle().apply {
                    putString(Constants.KEY_SEND_OTP, email)
                    openScreenWithContainer(
                        requireContext(),
                        if (type == Constants.KEY_SIGN_UP) NewAccountFragment::class.java else NewPasswordFragment::class.java,
                        this
                    )
                }
                binding.otpView.showSuccessState()
            }.addToBag()
            accountViewModel.rxMessage.subscribe {
                toast("Otp chưa đúng vui lòng thử lại !!")
                binding.otpView.showErrorState()
            }.addToBag()
            accountViewModel.isLoading.subscribe {
                if (it) showDialog()
                else hideDialog()
            }.addToBag()
        }
    }

    override fun getLayoutBinding(): FragmentEnterOtpBinding =
        FragmentEnterOtpBinding.inflate(layoutInflater)
}