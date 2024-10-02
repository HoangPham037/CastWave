package com.castwave.castwave.ui.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.OnItemClick
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Evaluate
import com.castwave.castwave.databinding.ItemEvaluateBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.setupTextWithSeeMore


class EvaluateAdapter : BaseAdapter<ItemEvaluateBinding, Evaluate>() {
    private var onItemClick: OnItemClick<Evaluate>? = null
    var isExpandedChapter = false
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemEvaluateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        val size = itemCountZ
        return if (isExpandedChapter) size else if (size > Constants.INDEX_1) Constants.INDEX_1 else size
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemEvaluateBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvName.text = item.name
            binding.tvTime.text = item.date
            binding.tvContentEvaluate.setupTextWithSeeMore(item.content, R.color.purple_01)
            binding.ratingBarContent.rating = item.rateContent
            binding.ratingBarVoice.rating = item.rateReadingVoice
            binding.tvTotalLike.text = item.listIdLike.size.toString()
            binding.ivAvatar.setImageCropCenter(item.avatar , cacheCategory= DiskCacheStrategy.DATA)
            binding.ivLike.setOnClickListener {
               if (item.listIdLike.indexOf(item.id) == -Constants.INDEX_1) item.listIdLike.add(item.id) else item.listIdLike.remove(item.id)
               notifyItemChanged(adapterPosition)
            }
            binding.ivLike.setImageLevel(if (item.listIdLike.indexOf(item.id) != -Constants.INDEX_1) Constants.INDEX_1 else Constants.INDEX_0)
        }
    }

    fun setOnItemClickListener(callback: (item: Evaluate?, pos: Int) -> Unit) {
        onItemClick = OnItemClick(callback)
    }
}
