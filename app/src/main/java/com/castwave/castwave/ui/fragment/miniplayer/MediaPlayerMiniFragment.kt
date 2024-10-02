package com.castwave.castwave.ui.fragment.miniplayer

import android.os.Bundle
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.databinding.MediaPlayerMiniViewBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce

class MediaPlayerMiniFragment: BaseFragment<MediaPlayerMiniViewBinding> (){
    override fun getLayoutBinding(): MediaPlayerMiniViewBinding {
        return MediaPlayerMiniViewBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        setupAction()
    }
    private fun setupAction() {
        binding.icClose.clickWithDebounce {
            rxBus.send(Buser(Constants.KEY_HIDE_SCREEN_PLAY_VIDEO, null))
        }
    }
}