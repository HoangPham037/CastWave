package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.CarouselPodCast
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.ChannelFollow
import com.castwave.castwave.data.model.LikedTopic
import com.castwave.castwave.data.model.ListenedRecently
import com.castwave.castwave.data.model.TopTrendingChannel
import com.castwave.castwave.data.model.TopTrendingEpisodes
import com.castwave.castwave.databinding.ItemChannelFollowBinding
import com.castwave.castwave.databinding.ItemDiscoveryNewHotBinding
import com.castwave.castwave.databinding.ItemLikedTopicBinding
import com.castwave.castwave.databinding.ItemListenedRecentlyBinding
import com.castwave.castwave.databinding.ItemTopTrendingChannelBinding
import com.castwave.castwave.databinding.ItemTrendingEpisodesBinding
import com.castwave.castwave.databinding.LayoutCategoryAudioBookBinding
import com.castwave.castwave.utils.extension.hide

class PodcastAdapter(private val data: MutableList<Any>) : BaseAdapter<ViewBinding, Any>() {
        private var event : ((type : Int , data : Any?) ->Unit)? = null
    companion object {
        const val TYPE_NEW_AND_TOP_TRENDING = 1
        const val TYPE_TOP_TRENDING_CHANNEL = 2
        const val TYPE_TOP_TRENDING_EPISODES = 3
        const val TYPE_LISTENED_RECENTLY = 4
        const val TYPE_CHANNEL_FOLLOW = 5
        const val TYPE_LIKED_TOPIC = 6
        const val TYPE_CATEGORY = 7
    }

    private val topTrendingChannelAdapter by lazy { TopTrendingChannelAdapter() }
    private val topTrendingEpisodesAdapter by lazy { TopTrendingEpisodesAdapter() }
    private val listenedRecentlyAdapter by lazy { ListenedRecentlyAdapter() }
    private val channelFollowAdapter by lazy { ChannelFollowAdapter() }
    private val likedTopicAdapter by lazy { LikedTopicAdapter() }
    private val categoryAdapter by lazy { CategoryAudiobookAdapter() }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NEW_AND_TOP_TRENDING -> BaseViewHolder(ItemDiscoveryNewHotBinding.inflate(inflater, parent, false))
            TYPE_TOP_TRENDING_CHANNEL -> BaseViewHolder(ItemTopTrendingChannelBinding.inflate(inflater, parent, false))
            TYPE_TOP_TRENDING_EPISODES -> BaseViewHolder(ItemTrendingEpisodesBinding.inflate(inflater, parent, false))
            TYPE_LISTENED_RECENTLY -> BaseViewHolder(ItemListenedRecentlyBinding.inflate(inflater, parent, false))
            TYPE_CHANNEL_FOLLOW -> BaseViewHolder(ItemChannelFollowBinding.inflate(inflater, parent, false))
            TYPE_LIKED_TOPIC -> BaseViewHolder(ItemLikedTopicBinding.inflate(inflater, parent, false))
            TYPE_CATEGORY -> BaseViewHolder(LayoutCategoryAudioBookBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun showData(
         carouselPodCast: CarouselPodCast,
         topTrendingChannel: TopTrendingChannel,
         topTrendingEpisodes: TopTrendingEpisodes,
         listenedRecently: ListenedRecently,
         likedTopic: LikedTopic,
         channelFollow: ChannelFollow,
         categoryPodCourse: CategoryBook,
    ) {
        data.clear()
        data.add(carouselPodCast)
        data.add(topTrendingChannel)
        data.add(topTrendingEpisodes)
        data.add(listenedRecently)
        data.add(channelFollow)
        data.add(likedTopic)
        data.add(categoryPodCourse)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ViewBinding>, position: Int) {
        when (holder.binding) {
            is ItemDiscoveryNewHotBinding -> {
                val data = data[position] as CarouselPodCast
                holder.binding.apply {
                    layoutHeader.itemHeader.hide()
                    autoSlideViewPager.apply {
                        setItems(data.carouselPodCast)
                    }
                }
            }

            is ItemTopTrendingChannelBinding -> {
                val data = data[position] as TopTrendingChannel
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    rcvTopTrendingChannel.setHasFixedSize(true)
                    rcvTopTrendingChannel.adapter = topTrendingChannelAdapter
                    event?.let { topTrendingChannelAdapter.setEvent(it) }
                    topTrendingChannelAdapter.items = data.topTrendingChannel
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_TOP_TRENDING_CHANNEL , null)
                    }
                }
            }

            is ItemTrendingEpisodesBinding -> {
                val data = data[position] as TopTrendingEpisodes
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    rcvTrendingEpisodes.setHasFixedSize(true)
                    rcvTrendingEpisodes.adapter = topTrendingEpisodesAdapter
                    event?.let { topTrendingEpisodesAdapter.setEvent(it) }
                    listenedRecentlyAdapter.items = data.topTrendingEpisodes
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_TOP_TRENDING_EPISODES , null)
                    }
                }
            }

            is ItemListenedRecentlyBinding -> {
                val data = data[position] as ListenedRecently
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    rcvTListenedRecently.setHasFixedSize(true)
                    rcvTListenedRecently.adapter = listenedRecentlyAdapter
                    event?.let { listenedRecentlyAdapter.setEvent(it) }
                    topTrendingEpisodesAdapter.items = data.listenedRecently
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_LISTENED_RECENTLY , null)
                    }
                }
            }


            is ItemChannelFollowBinding -> {
                val data = data[position] as ChannelFollow
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    rcvChannelFollow.setHasFixedSize(true)
                    rcvChannelFollow.adapter = channelFollowAdapter
                    event?.let { channelFollowAdapter.setEvent(it) }
                    channelFollowAdapter.items = data.channelFollow
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_CHANNEL_FOLLOW , null)
                    }
                }
            }

            is ItemLikedTopicBinding -> {
                val data = data[position] as LikedTopic
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    rcvLikedTopic.setHasFixedSize(true)
                    rcvLikedTopic.adapter = likedTopicAdapter
                    event?.let { likedTopicAdapter.setEvent(it) }
                    likedTopicAdapter.items = data.likedTopic
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_LIKED_TOPIC , null)
                    }
                }
            }

            is LayoutCategoryAudioBookBinding -> {
                val data = data[position] as CategoryBook
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader?.uppercase()
                    layoutHeader.tvDesc.hide()
                    rcvCategoryAudioBook.setHasFixedSize(true)
                    rcvCategoryAudioBook.adapter = categoryAdapter
                    event?.let { categoryAdapter.setEvent(it) }
                    categoryAdapter.items = data.categoryBook
                    layoutHeader.imgGoDetails.setOnClickListener {
                        event?.invoke(TYPE_CATEGORY , null)
                    }
                }
            }
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is CarouselPodCast -> TYPE_NEW_AND_TOP_TRENDING
            is TopTrendingChannel -> TYPE_TOP_TRENDING_CHANNEL
            is TopTrendingEpisodes -> TYPE_TOP_TRENDING_EPISODES
            is ListenedRecently -> TYPE_LISTENED_RECENTLY
            is ChannelFollow -> TYPE_CHANNEL_FOLLOW
            is LikedTopic -> TYPE_LIKED_TOPIC
            is CategoryBook -> TYPE_CATEGORY
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    fun setEvent(function: (type : Int , data : Any?) -> Unit) {
        event = function
    }
}
