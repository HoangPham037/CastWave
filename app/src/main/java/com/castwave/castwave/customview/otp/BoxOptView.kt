package com.castwave.castwave.customview.otp

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.castwave.castwave.R
import com.castwave.castwave.utils.FontCache
import com.castwave.castwave.utils.extension.color

class BoxOptView : FrameLayout {

    private var textView: TextView? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OtpTextView)
        generateViews()
        styles.recycle()
    }

    private fun generateViews() {
        val defaultOtpTextSize = Utils.getPixels(context, DEFAULT_OTP_TEXT_SIZE).toFloat()
        val textViewParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        textViewParams.gravity = Gravity.CENTER
        textView = TextView(context)
        textView?.gravity = Gravity.CENTER
        textView?.typeface = FontCache.getTypeface(context, "plus_jakarta_sans_bold.ttf")
        textView?.setTextColor(context.color(R.color.white))
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultOtpTextSize)
        this.addView(textView, textViewParams)
    }

    fun setText(value: String) {
        if (textView != null) {
            textView?.text = value
        }
    }

    fun setViewState(state: Int) {
        when (state) {
            ACTIVE -> this.setBackgroundResource(R.drawable.bgr_box_active)

            INACTIVE -> this.setBackgroundResource(R.drawable.bgr_box_inactive)

            ERROR -> this.setBackgroundResource(R.drawable.bgr_box_state_error)

            SUCCESS -> this.setBackgroundResource(R.drawable.bgr_box_state_success)

            else -> {
                // do nothing
            }
        }
    }

    companion object {
        const val ACTIVE = 1
        const val INACTIVE = 0
        const val ERROR = -1
        const val SUCCESS = 2
        private const val DEFAULT_OTP_TEXT_SIZE = 18f
    }

}