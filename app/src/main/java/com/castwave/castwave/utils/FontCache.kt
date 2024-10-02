package com.castwave.castwave.utils

import android.content.Context
import android.graphics.Typeface
import java.util.HashMap
object FontCache {
    private val sCachedFonts = HashMap<String, Typeface>()
    fun getTypeface(context: Context, assetPath: String): Typeface? {
        if (!sCachedFonts.containsKey(assetPath)) {
            val tf = Typeface.createFromAsset(context.assets, assetPath)
            sCachedFonts[assetPath] = tf
        }
        return sCachedFonts[assetPath]
    }
}
