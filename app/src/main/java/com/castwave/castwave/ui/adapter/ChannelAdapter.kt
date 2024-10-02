package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.ChapterPodcast
import com.castwave.castwave.databinding.ItemChannelBinding
import com.castwave.castwave.databinding.ItemPodcastBinding
import com.castwave.castwave.utils.extension.setImageCropCenter

class ChannelAdapter : BaseAdapter<ItemChannelBinding, ChapterPodcast>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemChannelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ItemChannelBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder) {
            binding.ivCoverPhoto.setImageCropCenter(item.ivCover , cacheCategory= DiskCacheStrategy.DATA)
        }
    }
}