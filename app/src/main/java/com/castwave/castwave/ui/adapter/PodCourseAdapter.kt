package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.CategoryBookListener
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.PodCourseItemClickListener
import com.castwave.castwave.base.TypeViewAllPodCourse
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.CarouselPodCourse
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.PodCourseForYou
import com.castwave.castwave.data.model.RecommendMentor
import com.castwave.castwave.data.model.TopTrending
import com.castwave.castwave.data.model.TopicPodCourse
import com.castwave.castwave.databinding.ItemDiscoveryNewHotBinding
import com.castwave.castwave.databinding.ItemRankPodCourseBinding
import com.castwave.castwave.databinding.ItemRecommendMentorBinding
import com.castwave.castwave.databinding.ItemRecommendPodCourseBinding
import com.castwave.castwave.databinding.ItemTopicPodCourseBinding
import com.castwave.castwave.databinding.LayoutCategoryAudioBookBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.screenWidth

class PodCourseAdapter(private val data: MutableList<Any>) : BaseAdapter<ViewBinding, Any>() {

    companion object {
        const val TYPE_CAROUSEL = 1
        const val TYPE_FOR_YOU = 2
        const val TYPE_TOP_TRENDING = 3
        const val TYPE_TOPIC = 4
        const val TYPE_RECOMMEND_MENTOR = 5
        const val TYPE_CATEGORY = 6
    }

    private val topTrendingAdapter by lazy { RankPodCourseAdapter() }
    private val forYouAdapter by lazy { RecommendPodCourseAdapter() }
    private val topicAdapter by lazy { RecommendPodCourseAdapter() }
    private val mentorRecommendAdapter by lazy { MentorRecommendAdapter() }
    private val categoryAdapter by lazy { CategoryAudiobookAdapter() }

    private var podCourseItemClickListener: PodCourseItemClickListener? = null
    private var podCourseClickListener: PodCourseClickListener? = null
    private var categoryBookListener: CategoryBookListener? = null

    fun setPodCourseItemClickListener(listener: PodCourseItemClickListener) {
        this.podCourseItemClickListener = listener
    }

    fun setPodCourseClickListener(listener: PodCourseClickListener) {
        this.podCourseClickListener = listener
    }

    fun setCategoryBookClickListener(listener: CategoryBookListener) {
        this.categoryBookListener = listener
    }

    fun unRegisterListener() {
        podCourseItemClickListener = null
        podCourseClickListener = null
        categoryBookListener = null
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CAROUSEL -> {
                val binding = ItemDiscoveryNewHotBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_FOR_YOU -> {
                val binding = ItemRecommendPodCourseBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_TOP_TRENDING -> {
                val binding = ItemRankPodCourseBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_TOPIC -> {
                val binding = ItemTopicPodCourseBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_RECOMMEND_MENTOR -> {
                val binding = ItemRecommendMentorBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_CATEGORY -> {
                val binding = LayoutCategoryAudioBookBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun showData(
        carouselPodCourse: CarouselPodCourse,
        podCourseForYou: PodCourseForYou,
        topTrending: TopTrending,
        topicPodCourse: TopicPodCourse,
        recommendMentor: RecommendMentor,
        categoryBook: CategoryBook,
    ) {
        data.clear()
        data.add(carouselPodCourse)
        data.add(podCourseForYou)
        data.add(topTrending)
        data.add(topicPodCourse)
        data.add(recommendMentor)
        data.add(categoryBook)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ViewBinding>, position: Int) {
        when (holder.binding) {
            is ItemDiscoveryNewHotBinding -> {
                val data = data[position] as CarouselPodCourse
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    layoutHeader.imgGoDetails.hide()
                    autoSlideViewPager.apply {
                        setItems(data.carouselPodCourse)
                    }
                    podCourseClickListener?.let { autoSlideViewPager.setListener(it) }
                }
            }

            is ItemRecommendPodCourseBinding -> {
                val data = data[position] as PodCourseForYou
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    layoutHeader.imgGoDetails.hide()
                    rcvRecommend.adapter = forYouAdapter
                    forYouAdapter.items = data.forYous
                    podCourseClickListener?.let { forYouAdapter.setListener(it) }
                }
            }

            is ItemRankPodCourseBinding -> {
                val data = data[position] as TopTrending
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.text = data.desc
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        podCourseItemClickListener?.onViewAllClicked(
                            TypeViewAllPodCourse.TOP_TRENDING
                        )
                    }
                    rcvRankPodCourse.adapter = topTrendingAdapter
                    topTrendingAdapter.items = data.topTrending
                    podCourseClickListener?.let { topTrendingAdapter.setListener(it) }
                }
            }

            is ItemTopicPodCourseBinding -> {
                val data = data[position] as TopicPodCourse
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.text = data.desc
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        podCourseItemClickListener?.onViewAllClicked(
                            TypeViewAllPodCourse.TOPIC
                        )
                    }
                    rcvTopic.adapter = topicAdapter
                    topicAdapter.setWithScreen(screenWidth() / 2 + 250)
                    topicAdapter.items = data.topicPodCourse
                    podCourseClickListener?.let { topicAdapter.setListener(it) }
                }
            }

            is ItemRecommendMentorBinding -> {
                val data = data[position] as RecommendMentor
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.text = data.desc
                    layoutHeader.imgGoDetails.hide()
                    rcvTopic.adapter = mentorRecommendAdapter
                    mentorRecommendAdapter.items = data.recommendMentor
                    podCourseItemClickListener?.let { mentorRecommendAdapter.setListener(it) }
                }
            }

            is LayoutCategoryAudioBookBinding -> {
                val data = data[position] as CategoryBook
                holder.binding.apply {
                    val titleUppercase = data.titleHeader?.uppercase()
                    layoutHeader.tvTitle.text = titleUppercase
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        podCourseItemClickListener?.onViewAllClicked(
                            TypeViewAllPodCourse.CATEGORY
                        )
                    }
                    layoutHeader.tvDesc.hide()
                    rcvCategoryAudioBook.setHasFixedSize(true)
                    rcvCategoryAudioBook.adapter = categoryAdapter
                    categoryAdapter.items = data.categoryBook
                    categoryBookListener?.let { categoryAdapter.setListener(it) }
                }
            }

            else -> throw IllegalArgumentException("Invalid data type")

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is CarouselPodCourse -> TYPE_CAROUSEL
            is PodCourseForYou -> TYPE_FOR_YOU
            is TopTrending -> TYPE_TOP_TRENDING
            is TopicPodCourse -> TYPE_TOPIC
            is RecommendMentor -> TYPE_RECOMMEND_MENTOR
            is CategoryBook -> TYPE_CATEGORY
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }
}