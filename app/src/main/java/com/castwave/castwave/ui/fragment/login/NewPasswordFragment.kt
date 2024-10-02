package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.request.PasswordRequest
import com.castwave.castwave.databinding.FragmentNewPasswordBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.onTextChanged
import com.castwave.castwave.utils.isValidPassword
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment<FragmentNewPasswordBinding>() {
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initAction()
    }

    private fun initAction() {
        binding.tvResetPassword.setOnClickListener {
            nextFragment()
        }
        binding.ivClose.setOnClickListener { onBackPressed() }
        binding.edtPassword.onTextChanged { password ->
            showHidePassword(password, binding.textInputLayoutOne)
        }
        binding.edtConfirmPassword.onTextChanged { password ->
            showHidePassword(password, binding.textInputLayoutTwo)
        }
    }

    private fun showHidePassword(password: String?, textInputLayout: TextInputLayout) {
        if (password?.isEmpty() == true) {
            textInputLayout.endIconDrawable = null
        } else {
            textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            textInputLayout.setEndIconDrawable(R.drawable.custom_password_eye)
        }
    }

    private fun nextFragment() {
        val tvPassword = binding.edtPassword.text.toString()
        val tvConfirmPassword = binding.edtConfirmPassword.text.toString()
        if (tvPassword.isEmpty() || tvConfirmPassword.isEmpty()) {
            toast(requireContext().getString(R.string.txt_password_null))
            return
        }
        if (!isValidPassword(tvPassword)) {
            toast(requireContext().getString(R.string.txt_content_create_password))
            return
        }
        if (tvPassword != tvConfirmPassword) {
            toast(requireContext().getString(R.string.txt_password_donot_match))
        } else {
            arguments?.getString(Constants.KEY_SEND_OTP)?.let { email ->
                resetPassword(PasswordRequest(email, tvPassword, null, "forgotPassword"))
            }
        }
    }

    private fun resetPassword(body: PasswordRequest) {
        viewModel.rxResetPassword.subscribe {
            openScreenWithContainer(
                requireContext(),
                ResultResetPasswordFragment::class.java,
                arguments
            )
            toast(it)
        }.addToBag()
        viewModel.rxMessage.subscribe {
            toast(it)
        }.addToBag()
        viewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
        viewModel.resetPassword(body)
    }

    override fun getLayoutBinding(): FragmentNewPasswordBinding =
        FragmentNewPasswordBinding.inflate(layoutInflater)
}