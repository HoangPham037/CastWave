package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemPodcourseBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.show


class RecommendPodCourseAdapter : BaseAdapter<ItemPodcourseBinding, PodCourses>() {

    private var withScreen = 0

    private lateinit var listner: PodCourseClickListener
    fun setListener(addListener: PodCourseClickListener) {
        this.listner = addListener
    }

    fun setWithScreen(newWithScreen: Int) {
        this.withScreen = newWithScreen
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemPodcourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemPodcourseBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.layoutItem.layoutParams.width =
                if (withScreen == 0) LayoutParams.MATCH_PARENT else withScreen
            binding.tvTitlePodCourse.text = item.title
            binding.tvPodCourse.show()
            binding.tvPodCourse.text = "PodCourse"
            binding.icPlayPodCourse.show()
            binding.tvTitlePodCourse.text = item.title
            binding.tvNameAuthor.show()
            binding.tvNameAuthor.text = item.author
            binding.icPlayPodCourse.clickWithDebounce {
                listner.onPlayPodCourseClicked()
            }
            itemView.clickWithDebounce {
                listner.onPodCourseClicked(type = TypeCarousel.POD_COURSE)
            }
            binding.imgPodCourse.setImageCropCenter(item.imageUrl, cacheCategory =  DiskCacheStrategy.NONE)
        }
    }
}