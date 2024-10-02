package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.SubCategory
import com.castwave.castwave.databinding.ItemTopicCommonBinding


class TopicCommonAdapter : BaseAdapter<ItemTopicCommonBinding, SubCategory>() {
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemTopicCommonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemTopicCommonBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvNameTopicBook.text = item.name
            itemView.setOnClickListener { onItemClick?.onItemClickListener(item, position) }
        }
    }

    private var onItemClick: OnItemClick<SubCategory>? = null
    fun setOnItemClickListener(callback: (item: SubCategory?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}
