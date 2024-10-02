package com.castwave.castwave.ui.fragment.book

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Chapter
import com.castwave.castwave.data.model.Evaluate
import com.castwave.castwave.databinding.FragmentBookDetailBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.adapter.ChapterAdapter
import com.castwave.castwave.ui.adapter.EvaluateAdapter
import com.castwave.castwave.ui.adapter.SimilarAdapter
import com.castwave.castwave.ui.fragment.media.PlayFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.setDrawableEnd
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : BaseFragment<FragmentBookDetailBinding>() {
    private val adapterChapter by lazy { ChapterAdapter() }
    private val adapterSimilar by lazy { SimilarAdapter() }
    private val adapterEvaluate by lazy { EvaluateAdapter() }
    private var evaluate: Evaluate? = null
    private var isSeeMore = false

    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        setupAdapterChapter()
        setupAdapterEvaluate()
        updateTextViewSeeMore(isSeeMore)
    }

    private fun setupAdapterEvaluate() {
        binding.rcvEvaluate.adapter = adapterEvaluate
        val listEvaluate = getListEvaluate()
        adapterEvaluate.items = listEvaluate
        listEvaluate.map { if (it.id == Constants.INDEX_1) evaluate = it }
        binding.tvComment.text =
            if (evaluate == null) "Để lại đánh giá của bạn" else "Chỉnh sửa đáng giá của bạn"
    }


    private fun initView() {
        binding.tvTitleHeader.text = String.format("Milk and honey")
        binding.mainAppbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val boolean = appBarLayout.totalScrollRange + verticalOffset == Constants.INDEX_0
            binding.constraint.setVisibility(!boolean)
            binding.tvTitleHeader.setVisibility(boolean)
        }
    }
    private fun initAction() {
        binding.tvSeeAll.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
            (activity as MainActivity).showBottomNav()
        }
        binding.tvPlay.setOnClickListener {
            openScreenWithContainer(requireContext() , PlayFragment::class.java , null)
        }
        binding.tvComment.setOnClickListener {
            BottomSheetDialogNewReviews(evaluate).show(
                childFragmentManager,
                BookDetailFragment::class.java.name
            )
        }
        binding.tvSeeMore.setOnClickListener {
            isSeeMore = !isSeeMore
            updateTextViewSeeMore(isSeeMore)
        }
    }

    private fun updateTextViewSeeMore(isSeeMore: Boolean) {
        binding.layoutInformation.layoutInformation.setVisibility(isSeeMore)
        binding.tvSeeMore.text = if (isSeeMore) resources.getString(
            R.string.txt_hide_see_more
        ) else resources.getString(R.string.txt_see_more)
        binding.tvContent.ellipsize = if (isSeeMore) null else TextUtils.TruncateAt.END
        binding.tvContent.maxLines =
            if (isSeeMore) Int.MAX_VALUE else Constants.COLLAPSE_LINE_CONTENT
        binding.tvSeeMore.setDrawableEnd(if (isSeeMore) R.drawable.ic_hide_see_more else R.drawable.ic_see_more)
    }

    private fun showBottomSheetDialog() {
        BottomSheetDialogAllReviews(getListEvaluate()).show(
            childFragmentManager,
            BookDetailFragment::class.java.name
        )
    }

    private fun setupAdapterChapter() {
        binding.rcvChapter.adapter = adapterChapter
        setupListChapterSeeMore(getListChapter())
    }

    private fun setupListChapterSeeMore(chapters: MutableList<Chapter>) {
        setupViewChild(true, chapters)
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
        binding.tvShowAllChapter.text = String.format(
            if (isSeeMore) "Xem tất cả ${chapters.size} chương" else resources.getString(R.string.txt_collapse_chapter)
        )
        binding.tvShowAllChapter.setDrawableEnd(if (isSeeMore) R.drawable.ic_see_more else R.drawable.ic_hide_see_more)
        adapterChapter.items = if (isSeeMore)
            chapters.subList(0, Constants.COLLAPSE_POD_COURSE_CHAPTER) else
            chapters.subList(0, chapters.size)
    }

    private fun getListEvaluate(): MutableList<Evaluate> {
        return arrayListOf(
            Evaluate(
                1,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                2,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                3,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                4,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                5,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                6,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                7,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                8,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                9,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                10,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                11,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                12,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                13,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                14,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
            Evaluate(
                15,
                R.id.ivAvatar,
                "Chiếc Lá Khô",
                "19/08/2024",
                getListLike(),
                4.5f,
                5.0f,
                "Hoàng tử bé được coi là một trong những tác phẩm văn học thơ mộng, ngây thơ và trong sáng nhất mọi thời đại"
            ),
        )
    }

    private fun getListLike(): ArrayList<Int> {
        return arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    }

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

    override fun getLayoutBinding(): FragmentBookDetailBinding =
        FragmentBookDetailBinding.inflate(layoutInflater)
}