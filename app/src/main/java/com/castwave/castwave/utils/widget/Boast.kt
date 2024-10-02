package com.castwave.castwave.utils.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.LayoutRes
import java.lang.ref.WeakReference

class Boast
private constructor(

    private val internalToast: Toast?
) {
    init {
        if (internalToast == null) throw NullPointerException("Boast.Boast(Toast) requires a non-null parameter.")
    }

    fun cancel() {
        internalToast!!.cancel()
    }

    @JvmOverloads
    fun show(cancelCurrent: Boolean = true) {
        if (cancelCurrent) {
            val cachedGlobalBoast = globalBoast
            cachedGlobalBoast?.cancel()
        }
        globalBoast = this
        internalToast!!.show()
    }

    companion object {
        @Volatile
        private var weakBoast: WeakReference<Boast>? = null

        private var globalBoast: Boast?
            get() = if (weakBoast == null) {
                null
            } else weakBoast!!.get()
            set(globalBoast) {
                weakBoast = WeakReference<Boast>(globalBoast)
            }

        @SuppressLint("ShowToast")
        fun makeText(context: Context, text: CharSequence, duration: Int): Boast {
            return Boast(Toast.makeText(context, text, duration))
        }

        @SuppressLint("ShowToast")
        @Throws(Resources.NotFoundException::class)
        fun makeText(context: Context, resId: Int, duration: Int): Boast {
            return Boast(Toast.makeText(context, resId, duration))
        }

        @SuppressLint("ShowToast")
        fun makeText(context: Context, text: CharSequence): Boast {
            return Boast(Toast.makeText(context, text, Toast.LENGTH_SHORT))
        }

        @SuppressLint("ShowToast")
        @Throws(Resources.NotFoundException::class)
        fun makeText(context: Context, resId: Int): Boast {
            return Boast(Toast.makeText(context, resId, Toast.LENGTH_SHORT))
        }

        @SuppressLint("ShowToast")
        @Throws(Resources.NotFoundException::class)
        fun makeCustom(context: Context, @LayoutRes layoutResId: Int): Boast {
            val toast = Toast(context)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            val inflater = LayoutInflater.from(context)
            toast.view = inflater.inflate(layoutResId, null)

            return Boast(toast)
        }

        fun showText(context: Context, text: CharSequence, duration: Int) {
            makeText(context, text, duration).show()
        }

        @Throws(Resources.NotFoundException::class)
        fun showText(context: Context, resId: Int, duration: Int) {
            makeText(context, resId, duration).show()
        }

        fun showText(context: Context, text: CharSequence) {
            makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        @Throws(Resources.NotFoundException::class)
        fun showText(context: Context, resId: Int) {
            makeText(context, resId, Toast.LENGTH_SHORT).show()
        }

        @Throws(Resources.NotFoundException::class)
        fun showCustom(context: Context, @LayoutRes layoutResId: Int) {
            makeCustom(context, layoutResId).show()
        }
    }
}
