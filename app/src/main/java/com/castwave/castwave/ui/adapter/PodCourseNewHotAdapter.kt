package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.ItemPodcourseBinding
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.screenWidth


class PodCourseNewHotAdapter : BaseAdapter<ItemPodcourseBinding, PodCourses>() {
    private val withScreen = screenWidth() / 2 + 250
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemPodcourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemPodcourseBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.layoutItem.layoutParams.width = withScreen
            binding.tvTitlePodCourse.text = item.title
            if (item.isNewBook) binding.tvNewBook.apply {
                visibility = View.VISIBLE
                text = "Sách nói mơi"
            }
            binding.tvRate.show()
            binding.tvRate.text = item.rateStar.toString()
            binding.icPlayPodCourse.show()
        }
    }
}