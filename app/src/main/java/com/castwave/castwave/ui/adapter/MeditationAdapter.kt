package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.BookTopic
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.databinding.ItemBookTopicBinding
import com.castwave.castwave.databinding.ItemCategoryBinding
import com.castwave.castwave.databinding.ItemMeditationBinding
import com.castwave.castwave.utils.extension.setImageCropCenter


class MeditationAdapter : BaseAdapter<ItemMeditationBinding, BookTopic>() {
    private var event : (() -> Unit)? = null
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemMeditationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemMeditationBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvVipCard.text = item.gold
            binding.ivCoverPhoto.setImageCropCenter(item.imageView , cacheCategory = DiskCacheStrategy.DATA)
            itemView.setOnClickListener {
                event?.invoke()
            }
        }
    }

    fun setEvent(event: () -> Unit) {
        this.event = event
    }
}
