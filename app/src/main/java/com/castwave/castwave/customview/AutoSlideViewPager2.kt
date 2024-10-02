package com.castwave.castwave.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.data.model.CommonData
import com.castwave.castwave.databinding.ViewAutoSlideViewpagerBinding
import com.castwave.castwave.ui.fragment.discovery.ViewPagerSlideAdapter
import com.castwave.castwave.ui.fragment.discovery.ViewPagerSlideListener
import kotlin.math.abs

class AutoSlideViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ViewPagerSlideListener {
    private var adapterViewPager: ViewPagerSlideAdapter? = null
    private var callback: ViewPager2.OnPageChangeCallback? = null
    private lateinit var binding: ViewAutoSlideViewpagerBinding
    private var hander = android.os.Handler(Looper.getMainLooper())
    private var initPosition = DEFAULT_POSITION

    private lateinit var listner: PodCourseClickListener
    fun setListener(addListener: PodCourseClickListener) {
        this.listner = addListener
    }

    var lastX = 0f

    init {
        initView()
    }

    companion object {
        const val TIME_AUTO_PLAY = 5000L
        const val TIME_ANIMATION_CHANGE_PAGE = 800L
        const val DEFAULT_POSITION = Int.MAX_VALUE / 2
        const val OFFSET_PADDING = 400L
    }

    private fun initView() {
        binding = ViewAutoSlideViewpagerBinding.inflate(LayoutInflater.from(context), this, true)
        setupViewPager()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViewPager() {
        binding.nestedScrollView.setOnTouchListener(object : OnTouchListener {
            var LEFT_RIGHT = 0 - OFFSET_PADDING
            var RIGHT_LEFT = OFFSET_PADDING
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                val x = motionEvent.x
                if (x > lastX) {
                    motionEvent.offsetLocation(LEFT_RIGHT.toFloat(), 0f)
                } else {
                    motionEvent.offsetLocation(RIGHT_LEFT.toFloat(), 0f)
                }
                lastX = x
                binding.viewpagerAutoSlide.dispatchTouchEvent(motionEvent)
                return false
            }
        })

        adapterViewPager = ViewPagerSlideAdapter(
            this
        )
        val screenWidth = resources.displayMetrics.widthPixels
        binding.viewpagerAutoSlide.apply {
            setPadding(screenWidth / 7, 0, screenWidth / 6, 0)
            adapter = adapterViewPager
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 5
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(1))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.81f + r * 0.12f
                }
            }
            setPageTransformer(compositePageTransformer)
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus.not()) stopAutoSlide() else playAutoSlide()
    }

    fun setItems(listData: List<CommonData>) {
        adapterViewPager?.items = listData.toMutableList()
        setupCallback(listData.size)
    }

    private val objectPageChange = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            playAutoSlide()
        }
    }

    private fun setupCallback(imageSize: Int) {
        binding.viewpagerAutoSlide.run {
            callback?.let {
                unregisterOnPageChangeCallback(it)
                callback = objectPageChange
                registerOnPageChangeCallback(callback!!)
            } ?: kotlin.run {
                callback = objectPageChange
                registerOnPageChangeCallback(callback!!)
            }
            initPosition = DEFAULT_POSITION - (DEFAULT_POSITION % imageSize)
            this.currentItem = initPosition
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoSlide()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding.viewpagerAutoSlide.currentItem >= Int.MAX_VALUE - 1000) {
            binding.viewpagerAutoSlide.currentItem = initPosition
        }
        if ((binding.viewpagerAutoSlide.adapter?.itemCount ?: 0) > 1) playAutoSlide()
    }

    private fun stopAutoSlide() {
        hander.removeCallbacks(autoSlideRunnable)
    }

    fun playAutoSlide() {
        hander.apply {
            removeCallbacks(autoSlideRunnable)
            postDelayed(autoSlideRunnable, TIME_AUTO_PLAY)
        }
    }

    override fun onClick(item: CommonData, position: Int, type: TypeCarousel) {
        listner.onPodCourseClicked(type)
    }

    override fun onPlayClicked() {
        listner.onPlayPodCourseClicked()
    }

    private val autoSlideRunnable = Runnable {
        try {
            fakeDrag(binding.viewpagerAutoSlide, true, TIME_ANIMATION_CHANGE_PAGE, 1)
        } catch (e: Exception) {
            println("error : ${e.message}")
        }
    }
}

fun fakeDrag(viewPager: ViewPager2, leftToRight: Boolean, duration: Long, numberOfPages: Int) {
    val pxToDrag: Int = viewPager.width
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        var currentPxToDrag: Float = (currentValue - previousValue).toFloat() * numberOfPages
        when {
            leftToRight -> {
                currentPxToDrag *= -(4f / 6f)
            }
        }
        viewPager.fakeDragBy(currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            viewPager.beginFakeDrag()
        }

        override fun onAnimationEnd(animation: Animator) {
            viewPager.endFakeDrag()
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    })
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.duration = duration
    animator.start()
}
