package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Library
import com.castwave.castwave.databinding.ItemLibraryBinding
import com.castwave.castwave.databinding.PopupMoreLibraryBinding
import com.castwave.castwave.utils.extension.clickWithDebounce

class LibraryAdapter: BaseAdapter<ItemLibraryBinding, Library>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
       return BaseViewHolder(ItemLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemLibraryBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvNameCategory.text = item.category
            binding.tvTitle.text = item.name
            binding.tvAuthor.text = item.author
            binding.imgDownload.setBackgroundResource(if (item.isDownload) R.drawable.ic_downloaded else R.drawable.ic_download)
            binding.imgMore.clickWithDebounce {
                val popupMore = BaseShowPopup(itemView.context, PopupMoreLibraryBinding::inflate)
                popupMore.show(binding.imgMore, -520, 0)
            }
        }
    }
}