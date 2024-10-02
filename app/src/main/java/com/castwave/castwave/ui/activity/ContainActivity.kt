package com.castwave.castwave.ui.activity

import android.content.Intent
import android.os.Bundle
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseActivity
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.databinding.ActivityContainBinding
import com.castwave.castwave.ui.fragment.login.LogInFragment
import com.castwave.castwave.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainActivity : BaseActivity<ActivityContainBinding>(), RxBus.OnMessageReceived {

    companion object {
        private var fragmentClazz: Class<*>? = null
        private var bundle: Bundle? = null
        private var instance: ContainActivity? = null

        @JvmStatic
        fun getInstance(fragmentClazz: Class<*>, bundle: Bundle?): ContainActivity? {
            Companion.fragmentClazz = fragmentClazz
            Companion.bundle = bundle
            return instance
        }
    }

    override fun getLayoutBinding(): ActivityContainBinding {
        return ActivityContainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        rxBus.registerBuser(this)
        instance = this
        open(false)
    }

    fun open(addBackStack: Boolean) {
        fragmentClazz?.let { openFragmentWithActivity(binding.frameLayout.id, it, bundle, addBackStack) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        val mainFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (mainFragment) {
            is LogInFragment -> {
                rxBus.send(Buser(Constants.KEY_FINISH_APP, Constants.VALUE_FINISH_APP))
                finish()
            }

            else -> super.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        fragmentClazz = null
        bundle = null
        instance = null
    }

    override fun onMessageReceived(buser: Buser?) {
        if (buser?.key == Constants.KEY_NAVIGATE_FRAGMENT ) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
