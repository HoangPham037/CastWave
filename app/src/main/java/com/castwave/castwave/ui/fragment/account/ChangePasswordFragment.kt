package com.castwave.castwave.ui.fragment.account

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.data.model.request.PasswordRequest
import com.castwave.castwave.databinding.FragmentChangePasswordBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.onTextChanged
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private var userInfo : User ?= null
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }
    private fun initView() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
              userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
        }
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvSave.setOnClickListener {
            changePassword()
        }
        binding.edtPassword.onTextChanged { password ->
            showHidePassword(password, binding.textInputLayoutOne)
        }
        binding.edtConfirmPassword.onTextChanged { password ->
            showHidePassword(password, binding.textInputLayoutTwo)
        }
        binding.edtOldPassword.onTextChanged { password ->
            showHidePassword(password, binding.textInputLayoutOldPassword)
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
    private fun changePassword() {
        val tvOldPassword = binding.edtOldPassword.text.toString()
        if (tvOldPassword.isEmpty()) {
            toast(requireContext().getString(R.string.txt_please_enter_old_password))
            return
        }
        val tvPassword = binding.edtPassword.text.toString()
        val tvNewPassword = binding.edtConfirmPassword.text.toString()
        if (tvNewPassword.isEmpty() || tvPassword.isEmpty()) {
            toast(requireContext().getString(R.string.txt_please_enter_password))
            return
        }
        if (tvPassword != tvNewPassword) {
            toast(requireContext().getString(R.string.txt_password_do_not_match))
            return
        }
        changePassword(tvNewPassword , tvOldPassword)
    }

    private fun changePassword(tvNewPassword: String,tvOldPassword: String) {
        userInfo?.Email?.let { email->
        viewModel.rxResetPassword.subscribe {
           toast(it)
        }.addToBag()
        viewModel.rxMessage.subscribe {
            toast(it)
        }.addToBag()
        viewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
            viewModel.resetPassword(PasswordRequest( email,tvNewPassword , tvOldPassword , "changePassword"))
        }
    }

    override fun getLayoutBinding(): FragmentChangePasswordBinding =
        FragmentChangePasswordBinding.inflate(layoutInflater)
}