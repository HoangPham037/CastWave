package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Song
import com.castwave.castwave.databinding.ItemSoundBinding

class SoundAdapter( private val event: (Int) -> Unit) : BaseAdapter<ItemSoundBinding, Song>() {
    private var id = 0
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemSoundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemSoundBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvName.text = item.name
            binding.ivCheckChoose.setImageResource(if (item.id == id) R.drawable.ic_check_choose_true else R.drawable.ic_check_choose_false)
            itemView.setOnClickListener {
                id = item.id
                event.invoke(item.id)
                notifyDataSetChanged()
            }
        }
    }
}