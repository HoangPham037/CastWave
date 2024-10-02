package com.castwave.castwave.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat

open class BaseDialog(context: Context, styleRes: Int, private val allowBack: Boolean = true) :
    Dialog(context, styleRes) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window?.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
    }

    override fun show() {
        dismiss()
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        super.show()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (allowBack) {
            dismiss()
        }
    }
}
