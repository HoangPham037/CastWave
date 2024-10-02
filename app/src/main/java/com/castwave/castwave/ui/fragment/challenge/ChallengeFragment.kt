package com.castwave.castwave.ui.fragment.challenge

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.User
import com.castwave.castwave.databinding.FragmentChallengeBinding
import com.castwave.castwave.ui.fragment.account.AccountFragment
import com.castwave.castwave.ui.fragment.account.NoAccountFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.ChallengeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragment : BaseFragment<FragmentChallengeBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private var userInfo: User? = null
    private val viewModel: ChallengeViewModel by viewModels()
    override fun updateUI(savedInstanceState: Bundle?) {
        setupTime()
        initViews()
        handleClick()

    }

    private fun initViews() {
        preferences.getString(Constants.KEY_USER_INFO)?.let { jsonUserInfo ->
            userInfo = Gson().fromJson(jsonUserInfo, User::class.java)
            binding.ivAvatar.setImageCropCenter(userInfo?.Avatar ,cacheCategory = DiskCacheStrategy.DATA )
        }
    }

    private fun setupTime() {
        binding.itemBadge.tvSeeRewards.show()
        binding.itemListeningLevel.tvNameBadge.hide()
        binding.tvTime.text = String.format("Còn ${viewModel.getTime()} tiếng")
    }

    override fun getLayoutBinding(): FragmentChallengeBinding {
        return FragmentChallengeBinding.inflate(layoutInflater)
    }

    private fun handleClick() {
        binding.ivAvatar.clickWithDebounce {
            openScreenWithContainer(requireContext(), if (userInfo == null ) NoAccountFragment::class.java else AccountFragment::class.java, null)
        }
    }
}