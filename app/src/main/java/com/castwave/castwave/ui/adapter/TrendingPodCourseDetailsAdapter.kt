package com.castwave.castwave.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemRankAudioBookDetailsBinding
import com.castwave.castwave.databinding.PopupMoreAudioBookBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setDrawableTop
import com.castwave.castwave.utils.extension.setImageCropCenter

class TrendingPodCourseDetailsAdapter : BaseAdapter<ItemRankAudioBookDetailsBinding, PodCourses>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemRankAudioBookDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ItemRankAudioBookDetailsBinding>,
        position: Int
    ) {
        val items = items[position]
        with(holder) {
            binding.layoutBookHorizontal.tvNameBook.text = items.title
            binding.layoutBookHorizontal.tvNameAuthor.text = items.author
            binding.layoutBookHorizontal.imgPodCourse.setImageCropCenter(
                items.imageUrl,
                cacheCategory = DiskCacheStrategy.DATA
            )
            binding.layoutBookHorizontal.imgMore.clickWithDebounce {
                val vipPopup = BaseShowPopup(itemView.context, PopupMoreAudioBookBinding::inflate)
                vipPopup.binding.tvPlay.clickWithDebounce { }
                vipPopup.binding.tvInfo.clickWithDebounce { }
                vipPopup.binding.tvFavourite.clickWithDebounce { }
                vipPopup.show(binding.layoutBookHorizontal.imgMore, -420, 0, Gravity.BOTTOM)
            }
            when (items.rank) {
                1 -> binding.tvRank.setDrawableTop(R.drawable.ic_top_1)
                2 -> binding.tvRank.setDrawableTop(R.drawable.ic_top_2)
                3 -> binding.tvRank.setDrawableTop(R.drawable.ic_top_3)
                else -> {
                    binding.tvRank.setDrawableTop(0)
                    binding.tvRank.textScaleX = 1F
                    binding.tvRank.text = items.rank.toString()
                }
            }
        }
    }
}