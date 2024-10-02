package com.castwave.castwave.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemBookHorizontalBinding
import com.castwave.castwave.databinding.PopupMoreAudioBookBinding
import com.castwave.castwave.utils.extension.setImageCropCenter

class TopicPodCourseAdapter : BaseAdapter<ItemBookHorizontalBinding, PodCourses>() {

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemBookHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ItemBookHorizontalBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder) {
            binding.tvNameBook.text = item.title
            binding.tvNameAuthor.text = item.author
            binding.imgMore.setOnClickListener {
                val popup =
                    BaseShowPopup(context = it.context, PopupMoreAudioBookBinding::inflate)
                popup.show(binding.imgMore, -420, 0, Gravity.BOTTOM)
            }
            binding.imgPodCourse.setImageCropCenter(
                item.imageUrl,
                cacheCategory = DiskCacheStrategy.DATA
            )
        }
    }
}