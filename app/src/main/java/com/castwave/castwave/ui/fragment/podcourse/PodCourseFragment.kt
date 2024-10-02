package com.castwave.castwave.ui.fragment.podcourse

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.marginLeft
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.CategoryBookListener
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.PodCourseItemClickListener
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.base.TypeViewAllPodCourse
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.data.model.CarouselPodCourse
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.CommonData
import com.castwave.castwave.data.model.Mentor
import com.castwave.castwave.data.model.PodCourseForYou
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.data.model.RecommendMentor
import com.castwave.castwave.data.model.TopTrending
import com.castwave.castwave.data.model.TopicPodCourse
import com.castwave.castwave.databinding.FragmentPodCourseBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.adapter.DiscoveryAdapter
import com.castwave.castwave.ui.adapter.PodCourseAdapter
import com.castwave.castwave.ui.fragment.category.BookTopicFragment
import com.castwave.castwave.ui.fragment.discovery.DiscoveryFragment
import com.castwave.castwave.ui.fragment.discovery.RankAudioBookFragment
import com.castwave.castwave.ui.fragment.discovery.TopicDetailsFragment
import com.castwave.castwave.ui.fragment.podcourse.podcoursedetail.PodCourseDetailsFragment
import com.castwave.castwave.ui.fragment.videoinfomentor.VideoInfoMentorFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.getStatusBarHeight
import com.castwave.castwave.utils.openScreenWithContainer

class PodCourseFragment :
    BaseFragment<FragmentPodCourseBinding>(),
    PodCourseItemClickListener,
    PodCourseClickListener,
    CategoryBookListener,
    RxBus.OnMessageReceived {
    private val adapter by lazy { PodCourseAdapter(mutableListOf()) }

    override fun getLayoutBinding(): FragmentPodCourseBinding {
        return FragmentPodCourseBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        rxBus.registerBuser(this)
        setupPaddingHeader()
        setupAdapter()

    }

    private fun setupPaddingHeader() {
        val headerPaddingTop = (context?.getStatusBarHeight()
            ?: requireContext().resources.getDimension(R.dimen._30dp)).toInt()
        val param = binding.toolbarLayout.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(
            binding.toolbarLayout.marginLeft,
            headerPaddingTop,
            binding.toolbarLayout.paddingRight,
            binding.toolbarLayout.paddingBottom
        )
        binding.toolbarLayout.layoutParams = param
    }

    private fun setupAdapter() {
        binding.rcvPocCourse.setHasFixedSize(true)
        binding.rcvPocCourse.adapter = adapter
        adapter.showData(
            CarouselPodCourse(
                1,
                titleHeader = "Moi va hot",
                carouselPodCourse = listNewAndHot()
            ),
            PodCourseForYou(
                1,
                titleHeader = "Podcourse danh cho ban",
                forYous = listPodCourse()
            ),
            TopTrending(
                1,
                "op thinh hanh hom nay",
                "Podcourse",
                topTrending = listPodCourse()
            ),
            TopicPodCourse(
                1,
                "KINH DOANH VA KHOI NGHIEP",
                "Nhung goc nhin moi ve",
                topicPodCourse = listPodCourse()
            ),
            RecommendMentor(
                1,
                titleHeader = "Mentor ban co the biet",
                recommendMentor = listMentorRecommend()
            ),
            CategoryBook(
                1,
                titleHeader = "Danh muc PodCourse",
                categoryBook = listCategoryAudioBook()
            )
        )
        binding.rcvPocCourse.recycledViewPool.setMaxRecycledViews(
            DiscoveryAdapter.TYPE_POD_COURSE_NEW,
            1
        )

    }

    override fun onViewAllClicked(type: TypeViewAllPodCourse) {
        when (type) {
            TypeViewAllPodCourse.TOP_TRENDING -> {
                Bundle().apply {
                    putInt(Constants.KEY_ADAPTER_TRENDING, Constants.VALUE_ADAPTER_TRENDING_2)
                    openFragment(R.id.framePodCourse, RankAudioBookFragment::class.java, this, true)
                    (activity as MainActivity).hideBottomNav()
                }
            }

            TypeViewAllPodCourse.TOPIC -> {
                Bundle().apply {
                    putInt(Constants.KEY_ADAPTER_TOPIC, Constants.VALUE_ADAPTER_TOPIC_2)
                    openFragment(R.id.framePodCourse, TopicDetailsFragment::class.java, this, true)
                    (activity as MainActivity).hideBottomNav()
                }
            }

            TypeViewAllPodCourse.CATEGORY -> {
                openFragment(R.id.framePodCourse, TopicDetailsFragment::class.java, null, true)
                (activity as MainActivity).hideBottomNav()
            }
        }
    }

    override fun onGoVideoInfoMentor() {
        openScreenWithContainer(requireActivity(), VideoInfoMentorFragment::class.java, null)
    }

    override fun onPodCourseClicked(type: TypeCarousel) {
        if (type == TypeCarousel.POD_COURSE) {
            openFragment(R.id.framePodCourse, PodCourseDetailsFragment::class.java, null, true)
            (activity as MainActivity).hideBottomNav()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.setPodCourseClickListener(this)
        adapter.setPodCourseItemClickListener(this)
        adapter.setCategoryBookClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        adapter.unRegisterListener()
    }

    override fun onPlayPodCourseClicked() {
        rxBus.send(Buser(Constants.KEY_SHOW_SCREEN, Constants.VALUE_SHOW_SCREEN_PLAY_VIDEO))
    }

    override fun categoryClicked() {
        openFragment(R.id.framePodCourse, BookTopicFragment::class.java, null, true)
    }


    override fun onMessageReceived(buser: Buser?) {
        if (buser?.values == Constants.VALUE_BACK_FROM_POD_COURSE) {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                activity?.let {
                    if (it is MainActivity) {
                        it.showFragment(Constants.INDEX_0, DiscoveryFragment::class.java)
                    }
                }
            }
        }
    }

    private fun listMentorRecommend(): ArrayList<Mentor> {
        return arrayListOf(
            Mentor(R.drawable.img_test, "Pham Van Hoang", "Chu tich, CEO, Tap doan PhamGroup"),
            Mentor(R.drawable.img_test, "Chiec La Kho", "Giam doc Tap doan ChiecGroup"),
            Mentor(R.drawable.img_test, "Nguyen Khanh Toan", "Pho giam doc Tap doan NguyenGroup"),
            Mentor(R.drawable.img_test, "Nguyen Thi Thao", "Thu ky Tap doan NguyenGroup")
        )
    }


    private fun listCategoryAudioBook(): ArrayList<CategoryAudiobook> {
        return arrayListOf(
            CategoryAudiobook(id = 1, name = "Tam ly hoc"),
            CategoryAudiobook(id = 2, name = "Ky nang"),
            CategoryAudiobook(id = 3, name = "Tu duy"),
            CategoryAudiobook(id = 4, name = "Van hoc"),
            CategoryAudiobook(id = 5, name = "Tam ly hoc"),
            CategoryAudiobook(id = 6, name = "Tam ly hoc"),
            CategoryAudiobook(id = 7, name = "Tam ly hoc"),
            CategoryAudiobook(id = 8, name = "Tam ly hoc"),
            CategoryAudiobook(id = 9, name = "Tam ly hoc"),

            )
    }

    private fun listNewAndHot(): ArrayList<CommonData> {
        return arrayListOf(
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2016/11/19/15/18/woman-1839798_960_720.jpg",
                type = 1
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg",
                type = 1
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/11/19/21/10/glasses-1052010_1280.jpg",
                type = 1
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/12/04/09/13/leaves-1076307_1280.jpg",
                type = 1
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2016/11/20/08/58/books-1842261_1280.jpg",
                type = 1
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/11/19/21/14/glasses-1052023_1280.jpg",
                type = 1
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
                type = 1
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