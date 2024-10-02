package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Badge
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemBadgeBinding

class BadgeAdapter : BaseAdapter<ItemBadgeBinding, Badge>() {

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(ItemBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemBadgeBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvNameBadge.text = item.name
            binding.progressBar.max = item.totalMission
            binding.ivLevel.setImageResource(item.imageView)
            binding.tvLevelBadge.text = item.level.toString()
            binding.progressBar.progress = item.missionComplete
            binding.tvRatio.text = String.format("${item.missionComplete}/${item.totalMission}")
            binding.tvContent.text = String.format("Hoàn thành ${item.missionComplete} nhiệm vụ ")
        }
    }

    private var onItemClick: OnItemClick<Badge>? = null

    fun setOnItemClickListener(callback: (item: Badge?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}