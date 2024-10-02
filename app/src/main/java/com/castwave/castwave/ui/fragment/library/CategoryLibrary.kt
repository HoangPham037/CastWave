package com.castwave.castwave.ui.fragment.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.databinding.ItemCategoryLibraryBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setBackgroundResources

class CategoryLibrary : BaseAdapter<ItemCategoryLibraryBinding, CategoryLibrarys>() {

    private var selectedPosition = -1
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemCategoryLibraryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(
        holder: BaseViewHolder<ItemCategoryLibraryBinding>,
        position: Int
    ) {
        val item = items[position]
        with(holder) {
            binding.tvTitle.text = item.title
            if (position == 0 && selectedPosition == -1 || position == selectedPosition)
                holder.itemView.setBackgroundResources(R.drawable.bgr_item_select)
            else
                holder.itemView.setBackgroundResources(R.drawable.bgr_item_un_select)

            itemView.clickWithDebounce {
                onItemClick?.onItemClickListener(item, position)
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    private var onItemClick: OnItemClick<CategoryLibrarys>? = null

    fun setOnItemClickListener(callback: (item: CategoryLibrarys?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}