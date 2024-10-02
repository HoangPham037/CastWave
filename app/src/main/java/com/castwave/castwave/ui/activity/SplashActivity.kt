package com.castwave.castwave.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.castwave.castwave.base.BaseActivity
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.databinding.ActivitySplashBinding
import com.castwave.castwave.ui.fragment.login.LogInFragment
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var preferences: Preferences
    private val viewModel: AccountViewModel by viewModels()
    override fun getLayoutBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        viewModel.rxVerifyAcc.subscribe {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }.addToBag()
        viewModel.rxMessage.subscribe {
            openScreenWithContainer(this, LogInFragment::class.java, null)
        }.addToBag()
        viewModel.verifyAccount()
    }

    private fun Disposable.addToBag() {
        bag.add(this)
    }
}