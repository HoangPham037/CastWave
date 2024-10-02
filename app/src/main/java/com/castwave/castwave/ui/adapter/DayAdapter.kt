package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Day
import com.castwave.castwave.databinding.ItemDayBinding


class DayAdapter : BaseAdapter<ItemDayBinding, Day>() {
    private var event : ( () -> Unit)? = null
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemDayBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvName.text = String.format("${item.name}  ${item.id}")
            itemView.setOnClickListener {
                event?.invoke()
            }
        }
    }

    fun setEvent(function: () -> Unit) {
        event = function
    }
}
