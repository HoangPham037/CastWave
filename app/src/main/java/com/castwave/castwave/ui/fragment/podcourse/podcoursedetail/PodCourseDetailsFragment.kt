package com.castwave.castwave.ui.fragment.podcourse.podcoursedetail

import android.os.Bundle
import android.text.TextUtils
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Chapter
import com.castwave.castwave.data.model.Evaluate
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.FragmentPodCourseDetailsBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.adapter.ChapterAdapter
import com.castwave.castwave.ui.adapter.EvaluateAdapter
import com.castwave.castwave.ui.adapter.RecommendPodCourseAdapter
import com.castwave.castwave.ui.fragment.media.PlayFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.invisible
import com.castwave.castwave.utils.extension.setDrawableEnd
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.utils.screenWidth
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

class PodCourseDetailsFragment : BaseFragment<FragmentPodCourseDetailsBinding>() {

    private val adapterChapter by lazy { ChapterAdapter() }
    private val adapterEvaluate by lazy { EvaluateAdapter() }
    private val podCourseRecommendAdapter by lazy { RecommendPodCourseAdapter() }

    override fun getLayoutBinding(): FragmentPodCourseDetailsBinding {
        return FragmentPodCourseDetailsBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        setupViewHeader()
        setupAdapter()
        setupActionSeeMoreContent()
        initAction()
    }

    private fun initAction() {
        binding.imgBack.clickWithDebounce {
            onBackPressed()
            (activity as MainActivity).showBottomNav()
        }
        binding.tvPlay.clickWithDebounce {
            openScreenWithContainer(requireActivity(), PlayFragment::class.java, null)
        }
    }

    private fun setupViewHeader() {
        binding.mainCollapsing.title = " "
        binding.mainAppbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange: Int = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.tvTitleHeader.show()
                    binding.tvTitleHeader.text = resources.getString(R.string.app_name)
                    binding.constraint.hide()
                    isShow = true
                } else if (isShow) {
                    binding.tvTitleHeader.invisible()
                    binding.tvTitleHeader.text = null
                    binding.constraint.show()
                    isShow = false
                }
            }
        })
    }

    private fun setupAdapter() {
        setupEvaluateAdapter()
        setupChapterAdapter()
        setupRecommendPodCourse()
    }

    private fun setupChapterAdapter() {
        binding.rcvChapter.adapter = adapterChapter
        setupListChapterSeeMore(getListChapter())
    }

    private fun setupRecommendPodCourse() {
        binding.rcvPodCourseRecommend.adapter = podCourseRecommendAdapter
//        podCourseRecommendAdapter.items = listPodCourse()
        podCourseRecommendAdapter.setWithScreen(screenWidth() / 2 + 250)
    }

    private fun setupEvaluateAdapter() {
        if (getListEvaluate().size > 0) {
            binding.layoutNoDataEvaluate.layoutNoData.hide()
            binding.layoutEvaluate.layoutEvaluate.show()
            binding.rcvEvaluate.show()
            binding.rcvEvaluate.adapter = adapterEvaluate
            adapterEvaluate.items = getListEvaluate()
            binding.tvSeeAllEvaluate.setVisibility(getListEvaluate().size > 1)

        } else {
            binding.layoutNoDataEvaluate.layoutNoData.show()
            binding.layoutEvaluate.layoutEvaluate.hide()
            binding.rcvEvaluate.hide()
        }

    }

    private fun setupListChapterSeeMore(chapters: MutableList<Chapter>) {
        if (chapters.size > Constants.COLLAPSE_POD_COURSE_CHAPTER) {
            binding.tvShowAllChapter.show()
            val sublist =
                chapters.subList(Constants.COLLAPSE_DEFAULT, Constants.COLLAPSE_POD_COURSE_CHAPTER)
            adapterChapter.items = sublist
            var isSeeMore = false
            binding.tvShowAllChapter.clickWithDebounce {
                setupViewChild(isSeeMore, chapters)
                isSeeMore = !isSeeMore
            }
        } else if (chapters.isNotEmpty()) {
            adapterChapter.items = chapters
            binding.tvShowAllChapter.hide()
        } else binding.tvShowAllChapter.hide()
    }

    private fun setupViewChild(isSeeMore: Boolean, chapters: MutableList<Chapter>) {
        binding.tvShowAllChapter.show()
        binding.tvShowAllChapter.text =
            if (isSeeMore) resources.getString(R.string.txt_see_more) else resources.getString(R.string.txt_hide_see_more)
        binding.tvShowAllChapter.setDrawableEnd(if (isSeeMore) R.drawable.ic_see_more else R.drawable.ic_hide_see_more)
        adapterChapter.items = if (isSeeMore)
            chapters.subList(0, Constants.COLLAPSE_POD_COURSE_CHAPTER) else
            chapters.subList(0, chapters.size)
    }

    private fun setupActionSeeMoreContent() {
        fun updateView(isCheck: Boolean, isSetText: Boolean) {
            binding.tvContent.maxLines =
                if (isCheck) Constants.COLLAPSE_LINE_CONTENT else Int.MAX_VALUE
            binding.tvContent.ellipsize = if (isCheck) TextUtils.TruncateAt.END else null
            if (isSetText) {
                binding.tvSeeMore.text =
                    if (isCheck) resources.getString(R.string.txt_see_more) else resources.getString(
                        R.string.txt_hide_see_more
                    )
                binding.tvSeeMore.setDrawableEnd(if (isCheck) R.drawable.ic_see_more else R.drawable.ic_hide_see_more)
            }
        }

        var isSeeMore = false
        binding.tvContent.post {
            val isExpandable = binding.tvContent.lineCount > Constants.COLLAPSE_LINE_CONTENT
            binding.tvSeeMore.setVisibility(isExpandable)
            updateView(isExpandable, false)
            binding.tvSeeMore.clickWithDebounce {
                updateView(isSeeMore, true)
                isSeeMore = !isSeeMore
            }
        }
    }
    private fun getListEvaluate(): MutableList<Evaluate> {
        return arrayListOf()
    }

//    private fun getListEvaluate(): MutableList<Evaluate> {
//        return arrayListOf(
//            Evaluate(
//                1,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại "
//            ),
//            Evaluate(
//                2,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                3,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                4,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                5,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                6,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                7,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                8,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                9,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                10,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                11,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                12,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                13,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                14,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//            Evaluate(
//                15,
//                R.id.ivAvatar,
//                "Chiếc Lá Khô",
//                "19/08/2024",
//                getListLike(),
//                4.5f,
//                5.0f,
//                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
//            ),
//        )
//    }

    private fun getListLike(): ArrayList<Int> {
        return arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    }

//    private fun listPodCourse(): ArrayList<PodCourses> {
//        return arrayListOf(
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 4.7f,
//                rank = 1,
//                type = false,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 4.5f,
//                rank = 2,
//                type = true,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 4f,
//                rank = 3,
//                type = false,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 5f,
//                rank = 4,
//                type = false,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 3.8f,
//                rank = 6,
//                type = true,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 3.8f,
//                rank = 7,
//                type = true,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 3.8f,
//                rank = 8,
//                type = false,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 3.8f,
//                rank = 9,
//                type = true,
//                author = "Pham van hoang"
//            ),
//            PodCourses(
//                title = "Milk and honey",
//                imageUrl = R.drawable.img_test,
//                isPodCourse = false,
//                isNewBook = true,
//                rateStar = 3.8f,
//                rank = 10,
//                type = false,
//                author = "Pham van hoang"
//            )
//        )
//    }

    private fun getListChapter(): MutableList<Chapter> {
        return arrayListOf(
            Chapter(1, true, "GIỚI THIỆU", "2 giờ 18 phút"),
            Chapter(
                2,
                false,
                "Chương 1: Làm chủ cái tôi cảm xúc - Quy luật của sự thiếu sáng suốt - Đấu tranh...",
                "2 giờ 18 phút"
            ),
            Chapter(3, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(4, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(5, false, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(6, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(7, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(8, false, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(9, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(10, false, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(11, false, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(12, true, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
            Chapter(13, false, "Chương 2: Quy luật của sự thiếu sáng suốt ", "2 giờ 18 phút"),
        )
    }
}