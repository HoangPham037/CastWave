package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.BookTopic
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.data.model.Chapter
import com.castwave.castwave.databinding.ItemBookTopicBinding
import com.castwave.castwave.databinding.ItemCategoryBinding
import com.castwave.castwave.databinding.ItemChapterBinding
import com.castwave.castwave.utils.Constants


class ChapterAdapter : BaseAdapter<ItemChapterBinding, Chapter>() {
    private var onItemClick: OnItemClick<Chapter>? = null
     var isExpandedChapter = false

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

//    override fun getItemCount(): Int {
//       val size = super.getItemCount()
//        return if (isExpandedChapter)  size else if (size > Constants.INDEX_4) Constants.INDEX_4 else size
//    }
    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemChapterBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvName.text = item.name
            binding.tvTime.text = item.time
            binding.ivPlay.setImageResource(if (item.gold) R.drawable.ic_lock else R.drawable.ic_play_32dp)
        }
    }
    fun setOnItemClickListener(callback: (item: Chapter?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}
