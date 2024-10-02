package com.castwave.castwave.ui.fragment.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemCategorySearchBinding
import com.castwave.castwave.utils.extension.setBackgroundResources

class CategorySearchAdapter : BaseAdapter<ItemCategorySearchBinding, CategorySearch>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemCategorySearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ItemCategorySearchBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder) {
            binding.tvTitleCategorySearch.text = item.title
            binding.appCompatImageView.setBackgroundResources(item.img)
            itemView.setOnClickListener {
                onItemClick?.onItemClickListener( item, position)
            }
        }
    }
    private var onItemClick: OnItemClick<CategorySearch>? = null

    fun setOnItemClickListener(callback: (item: CategorySearch?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}