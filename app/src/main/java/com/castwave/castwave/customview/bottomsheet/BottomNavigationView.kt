package com.castwave.castwave.customview.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.castwave.castwave.R
import com.castwave.castwave.databinding.LayoutBottomNavBinding
import com.castwave.castwave.utils.Constants.INDEX_0
import com.castwave.castwave.utils.Constants.INDEX_1
import com.castwave.castwave.utils.Constants.INDEX_2
import com.castwave.castwave.utils.Constants.INDEX_3
import com.castwave.castwave.utils.Constants.INDEX_4
import com.castwave.castwave.utils.extension.setBackgroundResources
import com.castwave.castwave.utils.extension.setDrawableTop
import com.castwave.castwave.utils.extension.setTextColors

class BottomNavigationView : FrameLayout {
    private lateinit var binding: LayoutBottomNavBinding
    private var onSelectItemChanged: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    @SuppressLint("ResourceAsColor")
    private fun init() {
        binding = LayoutBottomNavBinding.inflate(LayoutInflater.from(context), null, false)
        addView(binding.root)
        changeSelectMenu(INDEX_0)
        onClickItemListener()
    }

    fun changeSelectMenu(position: Int) {
        when (position) {
            INDEX_1 -> {
                setViewSelect(binding.tvLibrary, INDEX_1)
                unSelectView(binding.tvPodCourse, INDEX_2)
                unSelectView(binding.tvDiscover, INDEX_0)
                unSelectView(binding.tvSearch, INDEX_3)
                unSelectView(binding.tvChallenge, INDEX_4)
            }

            INDEX_2 -> {
                unSelectView(binding.tvLibrary, INDEX_1)
                setViewSelect(binding.tvPodCourse, INDEX_2)
                unSelectView(binding.tvDiscover, INDEX_0)
                unSelectView(binding.tvSearch, INDEX_3)
                unSelectView(binding.tvChallenge, INDEX_4)
            }

            INDEX_3 -> {
                unSelectView(binding.tvLibrary, INDEX_1)
                unSelectView(binding.tvPodCourse, INDEX_2)
                unSelectView(binding.tvDiscover, INDEX_0)
                setViewSelect(binding.tvSearch, INDEX_3)
                unSelectView(binding.tvChallenge, INDEX_4)
            }

            INDEX_4 -> {
                unSelectView(binding.tvLibrary, INDEX_1)
                unSelectView(binding.tvPodCourse, INDEX_2)
                unSelectView(binding.tvDiscover, INDEX_0)
                unSelectView(binding.tvSearch, INDEX_3)
                setViewSelect(binding.tvChallenge, INDEX_4)
            }

            else -> {
                unSelectView(binding.tvLibrary, INDEX_1)
                unSelectView(binding.tvPodCourse, INDEX_2)
                setViewSelect(binding.tvDiscover, INDEX_0)
                unSelectView(binding.tvSearch, INDEX_3)
                unSelectView(binding.tvChallenge, INDEX_4)
            }
        }
    }

    private fun setViewSelect(textView: TextView, index: Int) {
        textView.setTextColors(R.color.green_02)
        when (index) {
            INDEX_1 -> {
                textView.setDrawableTop(R.drawable.ic_library_green)
            }

            INDEX_2 -> {
                textView.setDrawableTop(R.drawable.ic_podcource_green)
            }

            INDEX_3 -> {
                textView.setDrawableTop(R.drawable.ic_search_green)
            }

            INDEX_4 -> {
                textView.setDrawableTop(R.drawable.ic_challenge_green)
            }

            else -> {
                textView.setDrawableTop(R.drawable.ic_discover)
                textView.setBackgroundResources(R.drawable.bgr_discover)
            }
        }
    }

    private fun unSelectView(textView: TextView, index: Int) {
        textView.setTextColors(R.color.white)
        when (index) {
            INDEX_1 -> {
                textView.setDrawableTop(R.drawable.ic_library)
            }

            INDEX_2 -> {
                textView.setDrawableTop(R.drawable.ic_pod_course)
            }

            INDEX_3 -> {
                textView.setDrawableTop(R.drawable.ic_search)
            }

            INDEX_4 -> {
                textView.setDrawableTop(R.drawable.ic_challenge)
            }

            else -> {
                textView.setDrawableTop(R.drawable.ic_discover)
                textView.setBackgroundResources(R.drawable.bg_oval_grey1)
            }
        }
    }

    private fun onClickItemListener() {
        binding.tvLibrary.setOnClickListener { onSelectItemChanged?.invoke(INDEX_1) }
        binding.tvPodCourse.setOnClickListener { onSelectItemChanged?.invoke(INDEX_2) }
        binding.tvDiscover.setOnClickListener { onSelectItemChanged?.invoke(INDEX_0) }
        binding.tvSearch.setOnClickListener { onSelectItemChanged?.invoke(INDEX_3) }
        binding.tvChallenge.setOnClickListener { onSelectItemChanged?.invoke(INDEX_4) }
    }

    fun setOnClickItemClickListener(onClickItemClickListener: (Int) -> Unit) {
        this.onSelectItemChanged = onClickItemClickListener
    }

}