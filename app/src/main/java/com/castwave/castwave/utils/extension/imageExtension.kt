package com.castwave.castwave.utils.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.castwave.castwave.R

fun ImageView.setImageCropCenter(
    imgSource: Any?,
    placeholder: Int = R.drawable.ic_google,
    error: Int = R.drawable.ic_facebook,
    cacheCategory: DiskCacheStrategy = DiskCacheStrategy.NONE
) {
    imgSource?.let {
        val requestOptions = RequestOptions()
            .placeholder(placeholder)
            .centerCrop()
            .diskCacheStrategy(cacheCategory)
            .skipMemoryCache(true)
            .error(error)
        Glide.with(this.context).load(it).apply(requestOptions).into(this)
    }
}