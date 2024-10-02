package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.PodCast
import com.castwave.castwave.databinding.ItemEpisodesTrendingBinding
import com.castwave.castwave.databinding.ItemRecentlyListenedBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.show


class ChannelFollowAdapter : BaseAdapter<ItemEpisodesTrendingBinding, PodCast>() {
    private var event : ((type : Int , data : Any?) ->Unit)? = null
    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        return BaseViewHolder(
            ItemEpisodesTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ItemEpisodesTrendingBinding>, position: Int) {
        val item = items[position]
        with(holder) {
            binding.tvNameAuthor.show()
            binding.tvNameAuthor.text = item.author
            binding.ivPodcast.setImageCropCenter(item.imageUrl , cacheCategory =  DiskCacheStrategy.DATA )
            itemView.clickWithDebounce { event?.invoke(PodcastAdapter.TYPE_CHANNEL_FOLLOW , item) }
        }
    }
    fun setEvent(function : (type: Int, data: Any?) -> Unit) {
        event = function
    }
}