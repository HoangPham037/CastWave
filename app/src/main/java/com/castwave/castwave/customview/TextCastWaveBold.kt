package com.castwave.castwave.customview

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.castwave.castwave.R
import com.castwave.castwave.utils.FontCache

class TextCastWaveBold : AppCompatTextView {
    private var isUnderline: Boolean = false

    constructor(context: Context) : super(context) {
        setTypeface(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setTypeface(context)
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setTypeface(context)
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.UnderlineText, 0, 0)
        isUnderline = typedArray.getBoolean(R.styleable.UnderlineText_isUnderline, false)
        if (isUnderline) {
            val span = SpannableString(text)
            span.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            text = span
        }
    }

    private fun setTypeface(context: Context) {
        val face = FontCache.getTypeface(context, "plus_jakarta_sans_bold.ttf")
        this.typeface = face
    }
}
