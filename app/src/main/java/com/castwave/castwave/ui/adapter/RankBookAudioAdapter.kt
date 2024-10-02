package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.PodCourseItemClickListener
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.databinding.ItemBookRankBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.screenWidth


class RankBookAudioAdapter : BaseAdapter<ItemBookRankBinding, Book>() {
    private val withScreen = screenWidth() / 2
    private lateinit var listner: DiscoveryItemClickListener
    fun setListener(addListener: DiscoveryItemClickListener) {
        this.listner = addListener
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemBookRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemBookRankBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.layoutItem.layoutParams.width = withScreen
            binding.tvTitlePodCourse.text = item.name
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
            binding.imgPodCourse.setImageCropCenter(
                item.imgUrl,
                cacheCategory = DiskCacheStrategy.DATA
            )
            itemView.clickWithDebounce {
                listner.onBookClicked()
            }
        }
    }
}