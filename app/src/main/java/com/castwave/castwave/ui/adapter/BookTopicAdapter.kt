package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.BookTopic
import com.castwave.castwave.databinding.ItemBookTopicBinding
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.setTextColors


class BookTopicAdapter : BaseAdapter<ItemBookTopicBinding, Book>() {
    private var onItemClick: OnItemClick<BookTopic>? = null
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemBookTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemBookTopicBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvVipCard.text = if (item.isFree) "MIEN PHi" else "MAT PHI"
            binding.tvVipCard.setTextColors(if (item.isFree) R.color.green_01 else R.color.yellow)
            binding.ivCoverPhoto.setImageCropCenter(
                item.imgUrl,
                cacheCategory = DiskCacheStrategy.DATA
            )
        }
    }

    fun setOnItemClickListener(callback: (item: BookTopic?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}
