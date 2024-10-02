@file:Suppress("Unused")

package com.castwave.castwave.utils.extension

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.castwave.castwave.base.DialogConfirm
import com.castwave.castwave.ui.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import java.util.Locale
import java.util.concurrent.TimeUnit

const val ANIM_DURATION = 300L

fun ViewGroup.inflateExt(layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

fun <V : View> V.clickWithDebounce(
    debounceTime: Long = 600L,
    action: (view: V) -> Unit
): Disposable =
    RxView.clicks(this)
        .throttleFirst(debounceTime, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .subscribe { action(this) }


val Context.isLandscape: Boolean get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun BottomSheetBehavior<*>.peekHeightAnimate(value: Int): Animator {
    return ObjectAnimator.ofInt(this, "peekHeight", value)
        .apply {
            duration = ANIM_DURATION
            start()
        }
}


fun Context.showDialogConfirm(
    styleRes: Int,
    resImage: Int,
    header: String,
    desc: String,
    textSubmit: String,
    textCancel: String,
    onSubmit: (() -> Unit)
) {
    DialogConfirm(
        this,
        styleRes,
        resImage,
        header,
        desc,
        textSubmit,
        textCancel
    ) {
        onSubmit.invoke()
    }.show()
}

fun View.heightAnimation(newHeight: Int): Animator {
    val startHeight = this.height
    val animator = ValueAnimator.ofInt(startHeight, newHeight)

    animator.addUpdateListener { valueAnimator ->
        val animatedValue = valueAnimator.animatedValue as Int
        val layoutParams = this.layoutParams
        layoutParams.height = animatedValue
        this.layoutParams = layoutParams
    }

    animator.duration = ANIM_DURATION
    animator.start()

    return animator
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun TextView.setDrawableStart(@DrawableRes start: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(start, 0, 0, 0)
}

fun TextView.setDrawableTop(@DrawableRes top: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, top, 0, 0)
}

fun TextView.setDrawableEnd(@DrawableRes end: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, end, 0)
}

fun TextView.setDrawableBottom(@DrawableRes bottom: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, bottom)
}

fun View.setBackgroundColors(@ColorRes resId: Int) =
    setBackgroundColor(ContextCompat.getColor(context, resId))

fun View.setBackgroundResources(@DrawableRes resId: Int) =
    setBackgroundResource(resId)

fun TextView.setTextColors(@ColorRes resId: Int) =
    setTextColor(ContextCompat.getColor(context, resId))

fun EditText.onTextChanged(text: (String?) -> Unit) = addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        text(s.toString())
    }
})
fun SeekBar.onStopTrackingTouch(onStopTrackingTouch: (SeekBar?) -> Unit){
    this.setOnSeekBarChangeListener(object  : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            // do nothing
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTrackingTouch(seekBar)
        }
    })
}
val Fragment.requireArguments: Bundle
    get() = arguments ?: throw Exception("No arguments found!")

fun ViewPager.onPageSelected(position: (Int?) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(position: Int) {
            position(position)
        }
    })
}

fun String.capitalizedWord(): String {
    val words = replace('_', ' ').toLowerCase(Locale.getDefault())
        .split(" ".toRegex())
        .dropLastWhile { it.isEmpty() }
        .toTypedArray()
    var aString = ""
    for (word in words) {
        aString = aString + word.substring(0, 1)
            .toUpperCase(Locale.getDefault()) + word.substring(1) + " "
    }
    return aString
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun postSticky(event: Any) {
    EventBus.getDefault().postSticky(event)
}

fun removeAllStickyEvents() {
    EventBus.getDefault().removeAllStickyEvents()
}

fun removeStickyEvents(event: Any) {
    EventBus.getDefault().removeStickyEvent(event)
}

fun register(subscriber: Any) {
    EventBus.getDefault().register(subscriber)
}

fun unregister(subscriber: Any) {
    EventBus.getDefault().unregister(subscriber)
}

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun TextView.setupTextWithSeeMore(value: String, @ColorRes id: Int) {
    val maxLines = 4
    val seeMoreText = "...Xem thêm"
    val seeLessText = "\nẨn bớt"
    val seeMoreColor = this.context.getColor(id)

    var isExpanded = false

    fun updateText() {
        if (isExpanded) {
            val fullTextWithSeeLess = "$value$seeLessText"
            val spannableString = SpannableString(fullTextWithSeeLess)
            spannableString.setSpan(
                ForegroundColorSpan(seeMoreColor),
                value.length,
                fullTextWithSeeLess.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannableString
            this.maxLines = Int.MAX_VALUE
        } else {
            this.maxLines = maxLines
            this.text = value
            this.post {
                val layout = this.layout
                if (layout != null && layout.lineCount > maxLines) {
                    val endIndex = layout.getLineEnd(maxLines - 1)
                    val truncatedText = value.substring(0, endIndex - seeMoreText.length).trim()
                    val truncatedWithSeeMore = "$truncatedText$seeMoreText"

                    val spannableString = SpannableString(truncatedWithSeeMore)
                    spannableString.setSpan(
                        ForegroundColorSpan(seeMoreColor),
                        truncatedWithSeeMore.length - seeMoreText.length,
                        truncatedWithSeeMore.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    this.text = spannableString
                }
            }
        }
    }

    this.text = value
    this.post {
        if (this.lineCount > maxLines) {
            updateText()
            this.setOnClickListener {
                isExpanded = !isExpanded
                updateText()
            }
        } else {
            this.text = value
        }
    }
}