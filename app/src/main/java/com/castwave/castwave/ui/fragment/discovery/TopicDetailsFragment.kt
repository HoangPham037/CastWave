package com.castwave.castwave.ui.fragment.discovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.databinding.FragmentTopicBookDetailsBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.adapter.BookBestSellerAdapter
import com.castwave.castwave.ui.adapter.TopicPodCourseAdapter
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.invisible
import com.castwave.castwave.utils.extension.setBackgroundResources
import com.castwave.castwave.utils.extension.show
import com.castwave.castwave.viewmodel.TopicBookDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicDetailsFragment : BaseFragment<FragmentTopicBookDetailsBinding>() {

    private val bookBestSellerAdapter by lazy { BookBestSellerAdapter() }
    private val topicPodCourseAdapter by lazy { TopicPodCourseAdapter() }
    private val topicBookDetailViewModel: TopicBookDetailsViewModel by viewModels()
    private var receiverType: Int = 0

    override fun getLayoutBinding(): FragmentTopicBookDetailsBinding {
        return FragmentTopicBookDetailsBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        arguments?.let {
            receiverType = it.getInt(Constants.KEY_ADAPTER_TOPIC)
        }
        setupAdapter()
        setupViewHeader()
        setupAction()

    }

    private fun setupAdapter() {
        binding.rcvTopicDetail.setHasFixedSize(true)
        if (receiverType == Constants.VALUE_ADAPTER_TOPIC_1) {
            binding.rcvTopicDetail.adapter =
                if (receiverType == 1) bookBestSellerAdapter else topicPodCourseAdapter
            topicBookDetailViewModel.isDisplayList.observe(viewLifecycleOwner) { isDisplayList ->
                binding.rcvTopicDetail.layoutManager = if (isDisplayList) LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                ) else GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                binding.imgStateDisplay.setBackgroundResources(if (isDisplayList) R.drawable.ic_table else R.drawable.ic_list)
                bookBestSellerAdapter.updateType(if (isDisplayList) 1 else 2)
            }
            bookBestSellerAdapter.items = listBook()
        } else  {
            binding.rcvTopicDetail.adapter = topicPodCourseAdapter
            binding.rcvTopicDetail.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )

            topicPodCourseAdapter.items = listPodCourse()
        }
    }

    private fun setupViewHeader() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            binding.toolbarLayout.title = " "
            binding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
                var isShow: Boolean = false
                var scrollRange: Int = -1

                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        binding.layoutHeader.show()
                        binding.layoutTitle.hide()
                        isShow = true
                    } else if (isShow) {
                        binding.layoutHeader.invisible()
                        binding.layoutTitle.show()
                        isShow = false
                    }
                }
            })
        }
    }

    private fun setupAction() {
        binding.imgBack.clickWithDebounce {
            onBackPressed()
            (activity as MainActivity).showBottomNav()
        }
        binding.imgStateDisplay.clickWithDebounce {
            topicBookDetailViewModel.updateValue(!topicBookDetailViewModel.isDisplayList.value!!)
        }

    }

    private fun listBook(): ArrayList<Book> {
        return arrayListOf(
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 1,
                date = "22/08/2024"

            ),
            Book(
                id = 1,
                isFree = true,
                imgUrl = "https://cdn.pixabay.com/photo/2016/06/01/06/26/open-book-1428428_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 2,
                date = "21/08/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2017/07/02/09/03/books-2463779_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 3,
                date = "20/08/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2015/11/19/21/10/glasses-1052010_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 4,
                date = "19/08/2024"
            ),
            Book(
                id = 1,
                isFree = true,
                imgUrl = "https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 5,
                date = "14/08/2024"
            ),
            Book(
                id = 1,
                isFree = true,
                imgUrl = "https://cdn.pixabay.com/photo/2015/12/04/09/13/leaves-1076307_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 6,
                date = "17/08/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2016/10/26/10/05/book-1771073_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 7,
                date = "12/08/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2016/03/09/15/29/books-1246674_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 8,
                date = "11/08/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2016/11/20/08/58/books-1842261_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 9,
                date = "11/09/2024"
            ),
            Book(
                id = 1,
                isFree = false,
                imgUrl = "https://cdn.pixabay.com/photo/2015/11/19/21/14/glasses-1052023_1280.jpg",
                name = "Doc vi nguoi khac",
                author = "Pham Van Hoang",
                rank = 10,
                date = "22/09/2024"
            ),
        )
    }

    private fun listPodCourse(): ArrayList<PodCourses> {
        return arrayListOf(
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://danviet.mediacdn.vn/296231569849192448/2022/7/18/z3575814842290f4a344b1db566b37fa96b7a2550a9801-1658133352416631936514.jpg",
                videoUrl = "link video",
                rateStar = 4.7f,
                isPodCourse = false,
                isNewBook = true,
                rank = 1,
                type = 2
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://media-cdn-v2.laodong.vn/Storage/NewsPortal/2021/10/30/969136/Cristiano-Ronaldo4.jpg",
                videoUrl = "link video",
                rateStar = 4.5f,
                isPodCourse = false,
                isNewBook = true,
                rank = 2,
                type = 2
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://aobongda.net/pic/Images/Module/News/images/4(3).jpg",
                videoUrl = "link video",
                rateStar = 4f,
                isPodCourse = false,
                isNewBook = true,
                rank = 3,
                type = 2
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://cafebiz.cafebizcdn.vn/2019/1/18/photo-1-154777686469242085471.jpeg",
                videoUrl = "link video",
                rateStar = 5f,
                isPodCourse = false,
                isNewBook = true,
                rank = 4,
                type = 2
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://photo.znews.vn/w660/Uploaded/kbd_pilk/2021_08_20/78.jpg",
                videoUrl = "link video",
                rateStar = 3.8f,
                isPodCourse = false,
                isNewBook = true,
                rank = 6,
                type = 1
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://image.dienthoaivui.com.vn/x,webp,q90/https://dashboard.dienthoaivui.com.vn/uploads/dashboard/editor_upload/hinh-nen-messi-4k-10.jpg",
                videoUrl = "link video",
                rateStar = 3.8f,
                isPodCourse = false,
                isNewBook = true,
                rank = 7,
                type = 1
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://danviet.mediacdn.vn/296231569849192448/2022/7/18/z3575814842290f4a344b1db566b37fa96b7a2550a9801-1658133352416631936514.jpg",
                videoUrl = "link video",
                rateStar = 3.8f,
                isPodCourse = false,
                isNewBook = true,
                rank = 8,
                type = 2
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://dienbientv.vn/dataimages/202108/original/images3067570_c75cd207_e3b1_4dc7_a5f5_431c0b99834c_1628203657304198409151.jpeg",
                videoUrl = "link video",
                rateStar = 3.8f,
                isPodCourse = false,
                isNewBook = true,
                rank = 9,
                type = 1
            ),
            PodCourses(
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://nld.mediacdn.vn/291774122806476800/2023/2/6/2486eec2-d846-41d0-ab19-4b348d63b5e1-16756864512721777446481.jpeg",
                videoUrl = "link video",
                rateStar = 3.8f,
                isPodCourse = false,
                isNewBook = true,
                rank = 10,
                type = 2
            )
        )
    }
}