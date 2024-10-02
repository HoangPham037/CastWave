package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemPodcourseBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.screenWidth


class RankPodCourseAdapter : BaseAdapter<ItemPodcourseBinding, PodCourses>() {
    private val withScreen = screenWidth() / 2 + 250
    private lateinit var listner: PodCourseClickListener
    fun setListener(addListener: PodCourseClickListener) {
        this.listner = addListener
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
            binding.layoutItem.layoutParams.width = withScreen
            binding.tvTitlePodCourse.text = item.title
            if (item.isNewBook) binding.tvNewBook.apply {
                visibility = View.VISIBLE
                text = "Sách nói mơi"
            }
            binding.tvPodCourse.show()
            binding.icPlayPodCourse.show()
            binding.tvNameAuthor.show()
            binding.tvNameAuthor.text = item.author
            binding.tvRank.show()
            binding.tvRank.text = item.rank.toString()
            when (item.rank) {
                1 -> binding.tvRank.setBackgroundResource(R.drawable.ic_rank_1)
                2 -> binding.tvRank.setBackgroundResource(R.drawable.ic_rank_2)
                3 -> binding.tvRank.setBackgroundResource(R.drawable.ic_rank_3)
                else -> binding.tvRank.setBackgroundResource(R.drawable.ic_rank_default)
            }
            binding.icPlayPodCourse.clickWithDebounce {
                listner.onPlayPodCourseClicked()
            }
            binding.imgPodCourse.setImageCropCenter(item.imageUrl, cacheCategory =  DiskCacheStrategy.DATA)
            itemView.clickWithDebounce {
                listner.onPodCourseClicked(TypeCarousel.POD_COURSE)
            }
        }
    }
}