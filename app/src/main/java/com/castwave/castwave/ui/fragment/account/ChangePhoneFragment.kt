package com.castwave.castwave.ui.fragment.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.data.model.request.UpdateProfileRequest
import com.castwave.castwave.databinding.FragmentChangePhoneBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePhoneFragment : BaseFragment<FragmentChangePhoneBinding>() {
    private val viewModel: AccountViewModel by viewModels()
    @Inject
    lateinit var preferences: Preferences
    private var userInfo: User? = null
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }
    private fun initView() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
            userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            userInfo?.let {
                binding.edtNewPhone.setText(it.PhoneNumber)
            }
        }
    }
    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvSave.setOnClickListener {
            changePhoneNumber()
        }
    }

    private fun changePhoneNumber() {
        val tvNewPhone = binding.edtNewPhone.text.toString()
        if (tvNewPhone.isEmpty()) {
            toast(requireContext().getString(R.string.txt_desc_finish_app))
            return
        }
        userInfo?.PhoneNumber = tvNewPhone
        updateProfile(UpdateProfileRequest(null , tvNewPhone))
    }

    private fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        viewModel.rxUpdateProfile.subscribe {
            toast(it.message)
            userInfo?.let { user ->
                preferences.setString(Constants.KEY_USER_INFO, Gson().toJson(user))
                onBackPressed()
            }
        }.addToBag()
        viewModel.rxMessage.subscribe { message ->
            toast(message)
        }.addToBag()
        viewModel.isLoading.subscribe {
            if (it) showDialog()
            else hideDialog()
        }.addToBag()
        viewModel.updateProfile(updateProfileRequest)
    }

    override fun getLayoutBinding(): FragmentChangePhoneBinding =
        FragmentChangePhoneBinding.inflate(layoutInflater)
}