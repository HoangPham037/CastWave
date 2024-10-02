package com.castwave.castwave.ui.fragment.account

import android.os.Bundle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.databinding.FragmentAccountBinding
import com.castwave.castwave.ui.fragment.setting.SettingFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.openScreenWithContainer
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    @Inject
    lateinit var preferences: Preferences
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
    }

    private fun initView() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
            val userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            binding.tvName.text = userInfo?.Name
            binding.ivAvatar.setImageCropCenter(
                userInfo?.Avatar,
                cacheCategory = DiskCacheStrategy.DATA
            )
        }
    }

    private fun initAction() {
        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
        binding.ivSetting.setOnClickListener {
            openScreenWithContainer(requireContext(), SettingFragment::class.java, null)
        }
        binding.tvTotalBadge.setOnClickListener {
            openScreenWithContainer(requireContext(), BadgeFragment::class.java, null)
        }
        binding.ivEdit.setOnClickListener {
            openScreenWithContainer(requireContext(), EditAccountFragment::class.java, null)
        }
    }

    override fun getLayoutBinding(): FragmentAccountBinding =
        FragmentAccountBinding.inflate(layoutInflater)
}