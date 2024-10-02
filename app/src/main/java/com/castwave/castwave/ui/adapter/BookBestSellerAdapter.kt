package com.castwave.castwave.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemBookHorizontalBinding
import com.castwave.castwave.databinding.ItemBookTopicBinding
import com.castwave.castwave.databinding.PopupMoreAudioBookBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.setTextColors

class BookBestSellerAdapter : BaseAdapter<ViewBinding, Book>() {
    companion object {
        const val TYPE_LIST = 1
        const val TYPE_TABLE = 2
    }

    private var typeChange = 1
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LIST -> {
                val binding = ItemBookHorizontalBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_TABLE -> {
                val binding = ItemBookTopicBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ViewBinding>, position: Int) {
        val item = items[position]
        when (holder.binding) {
            is ItemBookHorizontalBinding -> {
                val binding = holder.binding
                binding.tvTypePrice.text = if (item.isFree) "MIỄN PHÍ" else "MẤT PHÍ"
                binding.tvTypePrice.setTextColors(if (item.isFree) R.color.green_01 else R.color.yellow)
                binding.tvNameBook.text = item.name
                binding.tvNameAuthor.text = item.author
                binding.imgMore.setOnClickListener {
                    val popup =
                        BaseShowPopup(context = it.context, PopupMoreAudioBookBinding::inflate)
                    popup.show(binding.imgMore, -420, 0, Gravity.BOTTOM)
                }
                binding.imgPodCourse.clickWithDebounce {
                    listner.onBookClicked()
                }
                binding.imgPodCourse.setImageCropCenter(
                    item.imgUrl,
                    cacheCategory = DiskCacheStrategy.DATA
                )
            }

            is ItemBookTopicBinding -> {
                val binding = holder.binding
                binding.tvVipCard.text = if (item.isFree) "MẤT PHÍ" else "MIỄN PHÍ"
                binding.tvVipCard.setTextColors(if (item.isFree) R.color.green_01 else R.color.yellow)
                binding.ivMore.setOnClickListener {
                    val popup =
                        BaseShowPopup(context = it.context, PopupMoreAudioBookBinding::inflate)
                    popup.show(binding.ivMore, -420, 0, Gravity.BOTTOM)
                }
                binding.ivCoverPhoto.clickWithDebounce {
                    listner.onBookClicked()
                }
                binding.ivCoverPhoto.setImageCropCenter(
                    item.imgUrl,
                    cacheCategory = DiskCacheStrategy.DATA
                )
            }
        }
    }


    override fun getItemViewTypeZ(position: Int): Int {
        return when (typeChange) {
            1 -> TYPE_LIST
            2 -> TYPE_TABLE
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    fun updateType(typeChange: Int) {
        this.typeChange = typeChange
        notifyDataSetChanged()
    }
    private lateinit var listner: DiscoveryItemClickListener
    fun setListener(addListener: DiscoveryItemClickListener) {
        this.listner = addListener
    }
}