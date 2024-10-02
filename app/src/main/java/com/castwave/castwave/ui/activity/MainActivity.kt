package com.castwave.castwave.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseActivity
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.databinding.ActivityMainBinding
import com.castwave.castwave.ui.fragment.challenge.ChallengeFragment
import com.castwave.castwave.ui.fragment.discovery.DiscoveryFragment
import com.castwave.castwave.ui.fragment.library.LibraryFragment
import com.castwave.castwave.ui.fragment.media.PlayFragment
import com.castwave.castwave.ui.fragment.miniplayer.MediaPlayerMiniFragment
import com.castwave.castwave.ui.fragment.podcourse.PodCourseFragment
import com.castwave.castwave.ui.fragment.search.SearchFragment
import com.castwave.castwave.ui.fragment.videoinfomentor.VideoInfoMentorFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.Constants.INDEX_0
import com.castwave.castwave.utils.Constants.INDEX_1
import com.castwave.castwave.utils.Constants.INDEX_2
import com.castwave.castwave.utils.Constants.INDEX_3
import com.castwave.castwave.utils.Constants.INDEX_4
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.isLandscape
import com.castwave.castwave.utils.extension.peekHeightAnimate
import com.castwave.castwave.utils.extension.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_SETTLING
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), RxBus.OnMessageReceived {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private var miniPlayerFragment: MediaPlayerMiniFragment? = null
    private lateinit var slideInUp: Animation
    private lateinit var slideOutDown: Animation

    private val panelState: Int
        get() = bottomSheetBehavior.state

    override fun getLayoutBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        rxBus.registerBuser(this)
        handleAction()
        slideInUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom_view)
        slideOutDown = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom_view)
    }

    private fun showFragmentPlayer(fragment: Fragment) {
        showFragment(fragment)
        setupSlidingUpPanel()
        setupBottomSheet()
        expandPanel()
    }

    private fun cleanupBottomSheet() {
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallbackList)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = STATE_HIDDEN
    }

    private fun hideFragmentPlayer() {
        cleanupBottomSheet()
        hideFragment()

    }

    override fun onResume() {
        super.onResume()
        if (miniPlayerFragment != null) {
            if (bottomSheetBehavior.state == STATE_EXPANDED) {
                setMiniPlayerAlphaProgress(1f)
            }
        }
    }

    private fun setupBottomSheet() {
        val heightOfBarWithTabs =
            resources.getDimensionPixelSize(R.dimen._70dp) + resources.getDimensionPixelSize(R.dimen._70dp)
        bottomSheetBehavior = from(binding.slidingPanel)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallbackList)
        bottomSheetBehavior.significantVelocityThreshold = 300
        bottomSheetBehavior.peekHeight = heightOfBarWithTabs
        bottomSheetBehavior.isHideable = false
        setMiniPlayerAlphaProgress(0F)
    }

    private fun setupSlidingUpPanel() {
        binding.slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.slidingPanel.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                when (panelState) {
                    STATE_EXPANDED -> onPanelExpanded()
                    STATE_COLLAPSED -> onPanelCollapsed()
                    else -> {
                    }
                }
            }
        })
    }

    private fun expandPanel() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }

    private fun showFragment(fragment: Fragment) {
        val fragments = when (fragment) {
            is PlayFragment -> PlayFragment()
            else -> VideoInfoMentorFragment()
        }
        supportFragmentManager.commit {
            add(R.id.playerFragmentContainer, fragments)
        }

        binding.slidingPanel.show()
        miniPlayerFragment = MediaPlayerMiniFragment()
    }

    private fun hideFragment() {
        binding.slidingPanel.hide()
        miniPlayerFragment = null
    }

    private fun onPanelCollapsed() {
        setMiniPlayerAlphaProgress(0F)
    }

    private fun onPanelExpanded() {
        setMiniPlayerAlphaProgress(1F)
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (progress < 0) return
        val alpha = 1f - progress
        supportFragmentManager.findFragmentById(R.id.miniPlayerFragment)?.view?.let { view ->
            view.alpha = 1 - (progress / 0.2F)
            view.isGone = alpha == 0f
        }

        supportFragmentManager.findFragmentById(R.id.playerFragmentContainer)?.view?.let { view ->
            view.alpha = (progress - 0.2F) / 0.2F
            view.isGone = alpha == 1f
        }

        if (!isLandscape) {
            binding.layoutBottomNav.translationY = progress * 500
            binding.layoutBottomNav.alpha = alpha
        }

    }

    private var isSwipingDown = false
    private val bottomSheetCallbackList by lazy {
        object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                isSwipingDown = if (slideOffset < 0) {
                    true
                } else {
                    false
                }
                setMiniPlayerAlphaProgress(slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_EXPANDED -> {
                        onPanelExpanded()
                    }

                    STATE_COLLAPSED -> {
                        onPanelCollapsed()
                    }

                    STATE_SETTLING, STATE_DRAGGING -> {
                        if (isSwipingDown && newState == STATE_DRAGGING) {
                            bottomSheetBehavior.state = STATE_COLLAPSED
                        }
                    }

                    STATE_HIDDEN -> {
                        // do nothing
                    }

                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun handleAction() {
        openFragment(R.id.mainContainer, DiscoveryFragment::class.java, null, true)
        binding.layoutBottomNav.setOnClickItemClickListener { pos ->
            when (pos) {
                INDEX_1 -> showFragment(INDEX_1, LibraryFragment::class.java)
                INDEX_2 -> showFragment(INDEX_2, PodCourseFragment::class.java)
                INDEX_3 -> showFragment(INDEX_3, SearchFragment::class.java)
                INDEX_4 -> showFragment(INDEX_4, ChallengeFragment::class.java)
                else -> showFragment(INDEX_0, DiscoveryFragment::class.java)
            }
            binding.layoutBottomNav.changeSelectMenu(pos)
        }
        supportFragmentManager.findFragmentById(R.id.miniPlayerFragment)?.view?.let { view ->
            view.clickWithDebounce {
                expandPanel()
            }
        }
    }

    fun showBottomNav() {
        binding.layoutBottomNav.startAnimation(slideInUp)
        binding.layoutBottomNav.visibility = View.VISIBLE
        if (::bottomSheetBehavior.isInitialized) {
            val heightOfBarWithTabs =
                resources.getDimensionPixelSize(R.dimen._70dp) + resources.getDimensionPixelSize(R.dimen._70dp)
            bottomSheetBehavior.peekHeightAnimate(heightOfBarWithTabs)
        }
    }

    fun hideBottomNav() {
        binding.layoutBottomNav.startAnimation(slideOutDown)
        binding.layoutBottomNav.visibility = View.GONE
        if (::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior.peekHeightAnimate(resources.getDimensionPixelSize(R.dimen._70dp))
        }
    }

    fun showFragment(pos: Int, fragment: Class<*>) {
        openFragment(R.id.mainContainer, fragment, null, true)
        binding.layoutBottomNav.changeSelectMenu(pos)
    }

    override fun onBackPressed() {
        when (currentFragment) {
            is LibraryFragment -> {
                showFragment(INDEX_0, DiscoveryFragment::class.java)
            }

            is PodCourseFragment -> {
                rxBus.send(Buser(Constants.KEY_BACK, Constants.VALUE_BACK_FROM_POD_COURSE))
            }

            is DiscoveryFragment -> {
                rxBus.send(Buser(Constants.KEY_BACK, Constants.VALUE_BACK_DISCOVERY))

            }

            is SearchFragment -> {
                rxBus.send(Buser(Constants.KEY_BACK, Constants.VALUE_BACK_FROM_SEARCH))
            }

            is ChallengeFragment -> {
                showFragment(INDEX_0, DiscoveryFragment::class.java)
            }

            else -> super.onBackPressed()
        }
    }

    override fun onMessageReceived(buser: Buser?) {
        when (buser?.key) {
            Constants.KEY_FINISH_APP -> {
                finish()
            }

            Constants.KEY_SHOW_SCREEN -> {
                when (buser.values) {
                    Constants.VALUE_SHOW_SCREEN_PLAY_AUDIO -> {
                        showFragmentPlayer(PlayFragment())
                    }

                    Constants.VALUE_SHOW_SCREEN_PLAY_VIDEO -> {
                        showFragmentPlayer(VideoInfoMentorFragment())
                    }
                }
            }

            Constants.KEY_HIDE_SCREEN_PLAY_VIDEO -> {
                hideFragmentPlayer()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        rxBus.unRegisterBuser()
        if (::slideInUp.isInitialized) {
            slideInUp.cancel()
        }
        if (::slideOutDown.isInitialized) {
            slideOutDown.cancel()
        }
    }
}