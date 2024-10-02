package com.castwave.castwave.ui.fragment.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.data.model.request.UpdateProfileRequest
import com.castwave.castwave.databinding.FragmentChangeNameBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeNameFragment : BaseFragment<FragmentChangeNameBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private var userInfo: User? = null
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    private fun initView() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
            userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            userInfo?.let {
                binding.edtName.setText(it.Name)
            }
        }
    }

    private fun initAction() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvSave.setOnClickListener {
            changeUserName()
        }
    }

    private fun changeUserName() {
        val tvName = binding.edtName.text.toString().trim()
        if (tvName.isEmpty()) {
            toast(requireContext().getString(R.string.txt_please_enter_name))
            return
        }
        userInfo?.Name = tvName
        updateProfile(UpdateProfileRequest(tvName, null))
    }

    private fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        viewModel.rxUpdateProfile.subscribe {
            userInfo?.let { user ->
                preferences.setString(Constants.KEY_USER_INFO, Gson().toJson(user))
                toast(it.message)
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

    override fun getLayoutBinding(): FragmentChangeNameBinding =
        FragmentChangeNameBinding.inflate(layoutInflater)
}