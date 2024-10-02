package com.castwave.castwave.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.request.RegisterRequest
import com.castwave.castwave.databinding.FragmentNewAccountBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.onTextChanged
import com.castwave.castwave.utils.isValidPassword
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewAccountFragment : BaseFragment<FragmentNewAccountBinding>() {

    private val accountViewModel: AccountViewModel by viewModels()

    override fun updateUI(savedInstanceState: Bundle?) {
        initAction(arguments)
    }

    private fun initAction(bundle: Bundle?) {
        binding.ivClose.setOnClickListener { onBackPressed() }
        binding.tvSignUp.setOnClickListener {
            signUpAccount(bundle)
        }
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

    private fun signUpAccount(bundle: Bundle?) {
        val tvLoginName = binding.edtUserName.text.toString()
        val tvPassword = binding.edtPassword.text.toString()
        val tvConfirmPassword = binding.edtConfirmPassword.text.toString()
        if (tvLoginName.isEmpty() || tvPassword.isEmpty() || tvConfirmPassword.isEmpty()) {
            toast(requireContext().getString(R.string.txt_user_name_password_null))
            return
        }
        if (!isValidPassword(tvPassword)) {
            toast(requireContext().getString(R.string.txt_content_create_password))
            return
        }
        if (tvPassword != tvConfirmPassword) {
            toast(requireContext().getString(R.string.txt_password_donot_match))
        } else {
            val email = bundle?.getString(Constants.KEY_SEND_OTP)
            if (email != null) {
                registerAccount(email, tvLoginName, tvPassword)
            }
        }
    }

    private fun registerAccount(
        email: String,
        tvLoginName: String,
        tvPassword: String
    ) {
        val registerRequest =
            RegisterRequest(email = email, name = tvLoginName, password = tvPassword)
        accountViewModel.registerAccount(registerRequest)
        accountViewModel.rxRegisterAccount.subscribe {
            openScreenWithContainer(requireContext(), LogInFragment::class.java, null)
        }.addToBag()
        accountViewModel.rxMessage.subscribe { message ->
            toast(message)
        }.addToBag()
        accountViewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
    }

    override fun getLayoutBinding(): FragmentNewAccountBinding =
        FragmentNewAccountBinding.inflate(layoutInflater)
}