package com.castwave.castwave.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.castwave.castwave.ui.activity.ContainActivity
import com.castwave.castwave.ui.activity.ContainActivity.Companion.getInstance


fun openScreenWithContainer(
    context: Context,
    fragmentClazz: Class<*>,
    bundle: Bundle?,
) {
    getInstance(fragmentClazz, bundle)?.let {
        it.open(true)
    } ?: run {
        context.startActivity(Intent(context, ContainActivity::class.java))
    }
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    return password.matches(passwordRegex.toRegex())
}

fun expand(view: View) {
    view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val displayMetrics = view.context.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels
    view.layoutParams.height = 0
    view.visibility = View.VISIBLE

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height = if (interpolatedTime == 1f) {
                LinearLayout.LayoutParams.MATCH_PARENT
            } else {
                (screenHeight * interpolatedTime).toInt()
            }
            view.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = 300
    view.startAnimation(animation)
}

fun collapse(view: View) {
    val initialHeight = view.measuredHeight

    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                view.visibility = View.GONE
            } else {
                view.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    animation.duration = 300
    view.startAnimation(animation)
}

fun setupTime(millisUntilFinished: Long): String {
    val seconds = (millisUntilFinished + 1000L) / 1000
    val displayHours = seconds / 3600
    val displayMinutes = (seconds % 3600) / 60
    val displaySeconds = seconds % 60

    return if (displayHours > 0) {
        String.format("%2d:%02d:%02d", displayHours, displayMinutes, displaySeconds)
    } else {
        String.format("%02d:%02d", displayMinutes, displaySeconds)
    }
}

fun setupTimeProgress(millisUntilFinished: Long): String {
    val seconds = millisUntilFinished  / 1000
    val displayHours = seconds / 3600
    val displayMinutes = (seconds % 3600) / 60
    val displaySeconds = seconds % 60

    return if (displayHours > 0) {
        String.format("%2d:%02d:%02d", displayHours, displayMinutes, displaySeconds)
    } else {
        String.format("%02d:%02d", displayMinutes, displaySeconds)
    }
}

fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    return (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == serviceClass.name }

}