package com.castwave.castwave.ui.fragment.setting

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.data.model.request.LogoutRequest
import com.castwave.castwave.databinding.FragmentSettingBinding
import com.castwave.castwave.ui.fragment.login.LogInFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.deviceId
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.showDialogConfirm
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private val viewModel: AccountViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    private fun initView() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
          val  userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            binding.tvName.text = userInfo?.Name
            Glide.with(requireContext()).load(userInfo?.Avatar)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(false)
                .placeholder(R.drawable.iv_avatar)
                .error(R.drawable.iv_avatar)
                .into(binding.ivAvatar)
        }
    }

    private fun initAction() {
        binding.tvBack.setOnClickListener {
            onBackPressed()
        }
        handleClickLogout()
    }
    private fun handleClickLogout() {
        binding.tvLogOut.clickWithDebounce {
            requireContext().showDialogConfirm(
                R.style.DialogFullScreen,
                R.drawable.ic_header_logout,
                requireContext().getString(R.string.txt_header_logout),
                requireContext().getString(R.string.txt_desc_logout),
                requireContext().getString(R.string.txt_submit_logout),
                requireContext().getString(R.string.txt_cancel)
            ) {
                viewModel.rxLogout.subscribe {
                    GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .signOut()
                        .addOnCompleteListener(requireActivity()) {
                            preferences.removeWithKey(Constants.KEY_JWT_TOKEN)
                            preferences.removeWithKey(Constants.KEY_USER_INFO)
                            preferences.removeWithKey(Constants.KEY_GOOGLE_TOKEN)
                            openScreenWithContainer(
                                requireContext(),
                                LogInFragment::class.java,
                                null
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
                viewModel.logoutAccount(LogoutRequest(requireContext().deviceId()))
            }
        }
    }

    override fun getLayoutBinding(): FragmentSettingBinding =
        FragmentSettingBinding.inflate(layoutInflater)
}