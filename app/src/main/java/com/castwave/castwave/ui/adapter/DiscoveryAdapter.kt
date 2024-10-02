package com.castwave.castwave.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.base.BaseViewHolder
import com.castwave.castwave.base.CategoryBookListener
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.TypeViewAllDiscovery
import com.castwave.castwave.base.adapter.BaseAdapter
import com.castwave.castwave.data.model.BestSaleBook
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.FirstListenBook
import com.castwave.castwave.data.model.NewAndHot
import com.castwave.castwave.data.model.PodCoursesRecommend
import com.castwave.castwave.data.model.RankingBook
import com.castwave.castwave.data.model.RankingPodCourse
import com.castwave.castwave.databinding.ItemBookBestSellerBinding
import com.castwave.castwave.databinding.ItemDiscoveryFirstListenBinding
import com.castwave.castwave.databinding.ItemDiscoveryNewHotBinding
import com.castwave.castwave.databinding.ItemRankBookAudioBinding
import com.castwave.castwave.databinding.ItemRankPodCourseBinding
import com.castwave.castwave.databinding.ItemRecommendPodCourseBinding
import com.castwave.castwave.databinding.LayoutCategoryAudioBookBinding
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide

class DiscoveryAdapter(private val data: MutableList<Any>) : BaseAdapter<ViewBinding, Any>() {

    companion object {
        const val TYPE_POD_COURSE_NEW = 1
        const val TYPE_BOOK_FIRST_LISTEN = 2
        const val TYPE_RANK_BOOK = 3
        const val TYPE_RANK_POD_COURSE = 4
        const val TYPE_BOOK_BEST_SELLER = 5
        const val TYPE_RECOMMEND_POD_COURSE = 6
        const val TYPE_CATEGORY_BOOK = 7
    }

    private val firstListenBookAdapter by lazy { FirstListenBookAdapter() }
    private val rankPodCourse by lazy { RankPodCourseAdapter() }
    private val rankBookAudioAdapter by lazy { RankBookAudioAdapter() }
    private val bookBestSellerAdapter by lazy { BookBestSellerAdapter() }
    private val recommendPodCourseAdapter by lazy { RecommendPodCourseAdapter() }
    private val categoryAudiobookAdapter by lazy { CategoryAudiobookAdapter() }

    private var discoveryItemClickListener: DiscoveryItemClickListener ?= null
    private var podCourseClickListener: PodCourseClickListener ?= null
    private var categoryBookListener: CategoryBookListener ?= null

    fun setDiscoveryItemClickListener(listener: DiscoveryItemClickListener) {
        this.discoveryItemClickListener = listener
    }

    fun setPodCourseClickListener(listener: PodCourseClickListener) {
        this.podCourseClickListener = listener
    }

    fun setCategoryBookClickListener(listener: CategoryBookListener) {
        this.categoryBookListener = listener
    }

    fun unRegisterListener() {
        discoveryItemClickListener = null
        podCourseClickListener = null
        categoryBookListener = null
    }

    override fun onCreateViewHolderZ(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_POD_COURSE_NEW -> {
                val binding = ItemDiscoveryNewHotBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_BOOK_FIRST_LISTEN -> {
                val binding = ItemDiscoveryFirstListenBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_RANK_BOOK -> {
                val binding = ItemRankBookAudioBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_RANK_POD_COURSE -> {
                val binding = ItemRankPodCourseBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_BOOK_BEST_SELLER -> {
                val binding = ItemBookBestSellerBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_RECOMMEND_POD_COURSE -> {
                val binding = ItemRecommendPodCourseBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            TYPE_CATEGORY_BOOK -> {
                val binding = LayoutCategoryAudioBookBinding.inflate(inflater, parent, false)
                BaseViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun showData(
        newAndHot: NewAndHot,
        firstListenBook: FirstListenBook,
        rankingBook: RankingBook,
        rankingPodCourse: RankingPodCourse,
        bestSaleBook: BestSaleBook,
        podCoursesRecommend: PodCoursesRecommend,
        categoryBook: CategoryBook
    ) {
        data.clear()
        data.add(newAndHot)
        data.add(firstListenBook)
        data.add(rankingBook)
        data.add(rankingPodCourse)
        data.add(bestSaleBook)
        data.add(podCoursesRecommend)
        data.add(categoryBook)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolderZ(holder: BaseViewHolder<ViewBinding>, position: Int) {
        when (holder.binding) {
            is ItemDiscoveryNewHotBinding -> {
                val data = data[position] as NewAndHot
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    layoutHeader.imgGoDetails.hide()
                    autoSlideViewPager.apply {
                        setItems(data.newAndHot)
                    }
                    podCourseClickListener?.let { autoSlideViewPager.setListener(it) }
                }
            }

            is ItemDiscoveryFirstListenBinding -> {
                val data = data[position] as FirstListenBook
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.tvDesc.hide()
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.FIRST_LISTEN
                        )
                    }
                    rcvFirstListen.setHasFixedSize(true)
                    rcvFirstListen.adapter = firstListenBookAdapter
                    firstListenBookAdapter.items = data.firstListenBook
                    discoveryItemClickListener?.let { firstListenBookAdapter.setListener(it) }
                }
            }

            is ItemRankBookAudioBinding -> {
                val data = data[position] as RankingBook
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.BOOK_TRENDING
                        )
                    }
                    layoutHeader.tvDesc.hide()
                    rcvRankBookAudio.setHasFixedSize(true)
                    rcvRankBookAudio.adapter = rankBookAudioAdapter
                    rankBookAudioAdapter.items = data.rankingBook
                    discoveryItemClickListener?.let { rankBookAudioAdapter.setListener(it) }
                }
            }

            is ItemRankPodCourseBinding -> {
                val data = data[position] as RankingPodCourse
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.POD_COURSE_TRENDING
                        )
                    }
                    layoutHeader.tvDesc.text = data.desc
                    rcvRankPodCourse.setHasFixedSize(true)
                    rcvRankPodCourse.adapter = rankPodCourse
                    rankPodCourse.items = data.rankingPodCourse
                    podCourseClickListener?.let { rankPodCourse.setListener(it) }
                }
            }


            is ItemBookBestSellerBinding -> {
                val data = data[position] as BestSaleBook
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.BEST_SALE_BOOK
                        )
                    }
                    layoutHeader.tvDesc.hide()
                    rcvBestSeller.setHasFixedSize(true)
                    rcvBestSeller.adapter = bookBestSellerAdapter
                    bookBestSellerAdapter.items = data.bestSaleBook
                    discoveryItemClickListener?.let { bookBestSellerAdapter.setListener(it) }
                }

            }

            is ItemRecommendPodCourseBinding -> {
                val data = data[position] as PodCoursesRecommend
                holder.binding.apply {
                    layoutHeader.tvTitle.text = data.titleHeader
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.RECOMMEND_POD_COURSE
                        )
                    }
                    layoutHeader.tvDesc.text = data.desc
                    rcvRecommend.setHasFixedSize(true)
                    rcvRecommend.adapter = recommendPodCourseAdapter
                    recommendPodCourseAdapter.items = data.podCoursesRecommend
                    podCourseClickListener?.let { recommendPodCourseAdapter.setListener(it) }
                }
            }

            is LayoutCategoryAudioBookBinding -> {
                val data = data[position] as CategoryBook
                holder.binding.apply {
                    val titleUppercase = data.titleHeader?.uppercase()
                    layoutHeader.tvTitle.text = titleUppercase
                    layoutHeader.imgGoDetails.clickWithDebounce {
                        discoveryItemClickListener?.onViewAllClicked(
                            TypeViewAllDiscovery.CATEGORY_BOOK
                        )
                    }
                    layoutHeader.tvDesc.hide()
                    rcvCategoryAudioBook.setHasFixedSize(true)
                    rcvCategoryAudioBook.adapter = categoryAudiobookAdapter
                    categoryAudiobookAdapter.items = data.categoryBook
                    categoryBookListener?.let { categoryAudiobookAdapter.setListener(it) }
                }
            }

            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is NewAndHot -> TYPE_POD_COURSE_NEW
            is FirstListenBook -> TYPE_BOOK_FIRST_LISTEN
            is RankingBook -> TYPE_RANK_BOOK
            is RankingPodCourse -> TYPE_RANK_POD_COURSE
            is BestSaleBook -> TYPE_BOOK_BEST_SELLER
            is PodCoursesRecommend -> TYPE_RECOMMEND_POD_COURSE
            is CategoryBook -> TYPE_CATEGORY_BOOK
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }
}
