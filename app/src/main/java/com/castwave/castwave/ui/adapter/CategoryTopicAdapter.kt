package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Badge
import com.castwave.castwave.data.model.SubCategory
import com.castwave.castwave.databinding.ItemBadgeBinding
import com.castwave.castwave.databinding.ItemSubCategoryBinding

class CategoryTopicAdapter : BaseAdapter<ItemSubCategoryBinding, SubCategory>() {

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemSubCategoryBinding>, position: Int) {
        val item = items[position]
        with(holder) {
                binding.tvName.text = item.name
        }
    }
}