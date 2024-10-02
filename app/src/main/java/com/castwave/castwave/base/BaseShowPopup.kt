package com.castwave.castwave.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding

class BaseShowPopup<T : ViewBinding>(
    context: Context,
    private val inflater: (LayoutInflater) -> T
) : PopupWindow(context) {
    val binding: T = inflater(LayoutInflater.from(context))

    init {
        contentView = binding.root
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        elevation = 10f
    }

    fun show(anchor: View, xOff: Int = 0, yOff: Int = 0, gravity: Int = Gravity.NO_GRAVITY) {
        showAsDropDown(anchor, xOff, yOff, gravity)
    }
}