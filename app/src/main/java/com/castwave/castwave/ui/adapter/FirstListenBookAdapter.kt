package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemBookVerticalBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.color
import com.castwave.castwave.utils.extension.setImageCropCenter

class FirstListenBookAdapter : BaseAdapter<ItemBookVerticalBinding, Book>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemBookVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemBookVerticalBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvTypePrice.text = if (item.isFree) "MIỄN PHÍ" else  "MẤT PHÍ"
            binding.tvTypePrice.setTextColor(
                if (item.isFree) itemView.context.color(R.color.green_01) else itemView.context.color(
                    R.color.yellow
                )
            )
            binding.root.clickWithDebounce {
                listner.onBookClicked()
            }
          binding.imgPodCourse.setImageCropCenter(
                item.imgUrl,
                cacheCategory = DiskCacheStrategy.DATA
            )
        }
    }
    private lateinit var listner: DiscoveryItemClickListener
    fun setListener(addListener: DiscoveryItemClickListener) {
        this.listner = addListener
    }
}