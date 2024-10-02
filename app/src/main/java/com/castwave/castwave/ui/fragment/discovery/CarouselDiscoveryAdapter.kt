package com.castwave.castwave.ui.fragment.discovery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.CommonData
import com.castwave.castwave.databinding.ItemHotPodCourseBinding
import com.castwave.castwave.databinding.ItemNewAndTrendingPodcastBinding
import com.castwave.castwave.databinding.ItemNewBookBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter

class ViewPagerSlideAdapter(
    private var listener: ViewPagerSlideListener? = null
) : BaseAdapter<ViewBinding, CommonData>() {

    companion object {
        private const val TYPE_POD_COURSE = 1
        private const val TYPE_BOOK = 2
        private const val TYPE_PODCAST = 3
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_POD_COURSE -> {
                val binding = ItemHotPodCourseBinding.inflate(
                    inflater,
                    parent,
                    false
                )
                BaseViewHolder(binding)
            }

            TYPE_BOOK -> {
                val binding = ItemNewBookBinding.inflate(
                    inflater,
                    parent,
                    false
                )
                BaseViewHolder(binding)
            }
            TYPE_PODCAST -> BaseViewHolder(ItemNewAndTrendingPodcastBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ViewBinding>,
        position: Int
    ) {
        val item = items[position % items.size]
        when (holder.binding) {
            is ItemHotPodCourseBinding -> {
                val binding = holder.binding
                binding.imgPodCourse.setImageCropCenter(
                    item.imageUrl,
                    cacheCategory = DiskCacheStrategy.DATA
                )
                binding.root.clickWithDebounce {
                    listener?.onClick(item, position, TypeCarousel.POD_COURSE)
                }
                binding.icPlayPodCourse.clickWithDebounce { listener?.onPlayClicked() }
            }

            is ItemNewBookBinding -> {
                val binding = holder.binding
                binding.imgPodCourse.setImageCropCenter(
                    item.imageUrl,
                    cacheCategory = DiskCacheStrategy.DATA
                )
                binding.root.clickWithDebounce {
                    listener?.onClick(item, position, TypeCarousel.AUDIO_BOOK)
                }
                binding.icPlayBookAudio.clickWithDebounce { listener?.onPlayClicked() }
            }
            is ItemNewAndTrendingPodcastBinding -> {
                holder.binding.ivPodCast.setImageCropCenter(
                    item.imageUrl,
                    cacheCategory = DiskCacheStrategy.DATA
                )
                holder.binding.root.clickWithDebounce {
                    listener?.onClick(item, position, TypeCarousel.AUDIO_BOOK)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  items[position%items.size].type
    }
}

interface ViewPagerSlideListener {
    fun onClick(item: CommonData, position: Int, type: TypeCarousel)
    fun onPlayClicked()
}
