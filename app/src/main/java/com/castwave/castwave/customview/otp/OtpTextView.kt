package com.castwave.castwave.customview.otp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.castwave.castwave.R
import java.util.regex.Pattern

class OtpTextView : FrameLayout {

    private var boxItemViews: MutableList<BoxOptView>? = null
    private var otpEditText: OTPChildEditText? = null
    var otpListener: OtpListener? = null

    private var length: Int = 0

    private val filter: InputFilter
        get() = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Pattern.compile(
                        PATTERN
                    )
                        .matcher(source[i].toString())
                        .matches()
                ) {
                    return@InputFilter ""
                }
            }
            null
        }

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

    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OtpTextView)
        styleEditTexts(styles, attrs)
        styles.recycle()
    }

    private fun styleEditTexts(styles: TypedArray, attrs: AttributeSet?) {
        length = DEFAULT_LENGTH
        generateViews(styles, attrs)
    }

    private fun generateViews(styles: TypedArray, attrs: AttributeSet?) {
        boxItemViews = ArrayList()
        if (length > 0) {
            val otp = styles.getString(R.styleable.OtpTextView_otp)
            val width = styles.getDimension(
                R.styleable.OtpTextView_width,
                Utils.getPixels(context, DEFAULT_WIDTH).toFloat()
            ).toInt()
            val height = styles.getDimension(
                R.styleable.OtpTextView_height,
                Utils.getPixels(context, DEFAULT_HEIGHT).toFloat()
            ).toInt()
            val space = Utils.getPixels(context, DEFAULT_SPACE)
            val params = LinearLayout.LayoutParams(width, height)
            params.setMargins(space, space, space, space)


            val editTextLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editTextLayoutParams.gravity = Gravity.CENTER
            otpEditText = OTPChildEditText(context)
            otpEditText?.filters = arrayOf(filter, InputFilter.LengthFilter(length))
            setTextWatcher(otpEditText)
            addView(otpEditText, editTextLayoutParams)


            val linearLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val linearLayout = LinearLayout(context)

            addView(linearLayout, linearLayoutParams)

            for (i in 0 until length) {
                val itemView = BoxOptView(context, attrs)
                itemView.setViewState(BoxOptView.INACTIVE)
                linearLayout.addView(itemView, i, params)
                boxItemViews?.add(itemView)
            }
            if (otp != null) {
                setOtpCharacter(otp)
            } else {
                setOtpCharacter("")
            }
        } else {
            throw IllegalStateException("Please specify the length of the otp view")
        }
    }

    private fun setTextWatcher(otpChildEditText: OTPChildEditText?) {
        otpChildEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setOtpCharacter(s)
                setFocusEdtOtp(s.length)
                otpListener?.let { otpListener ->
                    if (s.length == length) {
                        otpListener.onOtpFinish(s.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun setFocusEdtOtp(length: Int) {
        boxItemViews?.let { itemViews ->
            for (i in itemViews.indices) {
                if (i < length) {
                    itemViews[i].setViewState(BoxOptView.ACTIVE)
                } else {
                    itemViews[i].setViewState(BoxOptView.INACTIVE)
                }
            }
        }
    }

    fun setOtpCharacter(s: CharSequence) {
        boxItemViews?.let { itemViews ->
            for (i in itemViews.indices) {
                if (i < s.length) {
                    itemViews[i].setText(s[i].toString())
                } else {
                    itemViews[i].setText("")
                }
            }
        }
    }

    fun requestFocusOTP() {
        otpEditText?.requestFocus()
    }

    fun showErrorState() {
        boxItemViews?.let { itemViews ->
            for (itemView in itemViews) {
                itemView.setViewState(BoxOptView.ERROR)
            }
        }
    }

    fun showSuccessState() {
        boxItemViews?.let { itemViews ->
            for (itemView in itemViews) {
                itemView.setViewState(BoxOptView.SUCCESS)
            }
        }
    }

    private fun setOtpCharacter(otp: String) {
        otpEditText?.setText(otp)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(l: OnTouchListener) {
        super.setOnTouchListener(l)
        otpEditText?.setOnTouchListener(l)
    }

    companion object {

        private const val DEFAULT_LENGTH = 4
        private const val DEFAULT_HEIGHT = 48
        private const val DEFAULT_WIDTH = 48
        private const val DEFAULT_SPACE = 8
        private const val PATTERN = "[1234567890]*"
    }
}