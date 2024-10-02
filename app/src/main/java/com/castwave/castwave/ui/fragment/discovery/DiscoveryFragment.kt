package com.castwave.castwave.ui.fragment.discovery

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.marginLeft
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.CategoryBookListener
import com.castwave.castwave.base.DiscoveryItemClickListener
import com.castwave.castwave.base.PodCourseClickListener
import com.castwave.castwave.base.TypeCarousel
import com.castwave.castwave.base.TypeViewAllDiscovery
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.data.local.Preferences
import com.castwave.castwave.data.model.BestSaleBook
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.CommonData
import com.castwave.castwave.data.model.FirstListenBook
import com.castwave.castwave.data.model.NewAndHot
import com.castwave.castwave.data.model.PodCourses
import com.castwave.castwave.data.model.PodCoursesRecommend
import com.castwave.castwave.data.model.RankingBook
import com.castwave.castwave.data.model.RankingPodCourse
import com.castwave.castwave.databinding.FragmentDiscoveryBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.adapter.DiscoveryAdapter
import com.castwave.castwave.ui.fragment.account.AccountFragment
import com.castwave.castwave.ui.fragment.book.BookDetailFragment
import com.castwave.castwave.ui.fragment.category.BookTopicFragment
import com.castwave.castwave.ui.fragment.category.CategoryAudiobookFragment
import com.castwave.castwave.ui.fragment.podcourse.PodCourseFragment
import com.castwave.castwave.ui.fragment.podcourse.podcoursedetail.PodCourseDetailsFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.getStatusBarHeight
import com.castwave.castwave.utils.extension.setImageCropCenter
import com.castwave.castwave.utils.extension.showDialogConfirm
import com.castwave.castwave.utils.openScreenWithContainer
import com.castwave.castwave.viewmodel.AccountViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscoveryFragment : BaseFragment<FragmentDiscoveryBinding>(),
    DiscoveryItemClickListener,
    PodCourseClickListener,
    CategoryBookListener,
    RxBus.OnMessageReceived {
    @Inject
    lateinit var preferences: Preferences
    private val viewModel: AccountViewModel by viewModels()
    private val discoveryAdapter by lazy { DiscoveryAdapter(mutableListOf()) }
    override fun updateUI(savedInstanceState: Bundle?) {
        rxBus.registerBuser(this)
        setupPaddingHeader()
        setupAdapter()
        binding.imgStore.clickWithDebounce {
            //test
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }
        initAction()
    }

    private fun setupAdapter() {
        binding.rcvDiscovery.setHasFixedSize(true)
        binding.rcvDiscovery.adapter = discoveryAdapter
        discoveryAdapter.showData(
            NewAndHot(1, "Moi va hot", newAndHot = listNewAndHot()),
            FirstListenBook(
                1,
                titleHeader = "Tuyet cho lan nghe dau",
                firstListenBook = listBook()
            ),
            RankingBook(1, "Top sach noi thinh", "", rankingBook = listBook()),
            RankingPodCourse(
                1,
                "Podcourse",
                "Top thinh hanh hom nay",
                rankingPodCourse = listPodCourse()
            ),
            BestSaleBook(1, "Bán chạy nhất mọi thơi đại", bestSaleBook = listBook()),
            PodCoursesRecommend(
                1,
                "Goi y cho ban",
                desc = "podcourse",
                podCoursesRecommend = listPodCourse()
            ),
            CategoryBook(1, "Danh muc sach noi", categoryBook = listCategoryAudioBook())
        )
        binding.rcvDiscovery.recycledViewPool.setMaxRecycledViews(
            DiscoveryAdapter.TYPE_POD_COURSE_NEW,
            1
        )
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

    private fun initAction() {
        binding.imgAvatar.setOnClickListener {
            openScreenWithContainer(requireContext(), AccountFragment::class.java, null)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.rxVerifyAcc.subscribe { response ->
            binding.tvName.text = response.user.Name
            binding.imgAvatar.setImageCropCenter(
                response.user.Avatar,
                cacheCategory = DiskCacheStrategy.DATA
            )
            if (preferences.getString(Constants.KEY_USER_INFO) == null) {
                preferences.setString(Constants.KEY_USER_INFO, Gson().toJson(response.user))
            }
        }.addToBag()
        viewModel.rxMessage.subscribe {
            toast(it)
        }.addToBag()
        viewModel.verifyAccount()
        discoveryAdapter.setPodCourseClickListener(this)
        discoveryAdapter.setDiscoveryItemClickListener(this)
        discoveryAdapter.setCategoryBookClickListener(this)
    }

    override fun getLayoutBinding(): FragmentDiscoveryBinding {
        return FragmentDiscoveryBinding.inflate(layoutInflater)
    }

    override fun onBookClicked() {
        openScreenWithContainer(requireContext(), BookDetailFragment::class.java, null)
    }

    override fun onPodCourseClicked(type: TypeCarousel) {
        if (type == TypeCarousel.AUDIO_BOOK) {
            openFragment(R.id.frameDiscovery, BookDetailFragment::class.java, null, true)
            (activity as MainActivity).hideBottomNav()
        } else {
            openFragment(R.id.frameDiscovery, PodCourseDetailsFragment::class.java, null, true)
            (activity as MainActivity).hideBottomNav()
        }
    }

    override fun onMessageReceived(buser: Buser?) {
        if (buser?.values == Constants.VALUE_BACK_DISCOVERY) {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
                (activity as MainActivity).showBottomNav()
            } else {
                activity?.let {
                    if (it is MainActivity) {
                        it.showDialogConfirm(
                            R.style.DialogFullScreen,
                            R.drawable.ic_header_finish_app,
                            this.getString(R.string.txt_header_finish_app),
                            this.getString(R.string.txt_desc_finish_app),
                            this.getString(R.string.txt_cancel_finish_app),
                            this.getString(R.string.txt_submit_finish_app)
                        ) {
                            it.finish()
                        }
                    }
                }
            }
        }
    }

    override fun onPlayPodCourseClicked() {
        rxBus.send(Buser(Constants.KEY_SHOW_SCREEN, Constants.VALUE_SHOW_SCREEN_PLAY_AUDIO))
    }

    override fun categoryClicked() {
        openScreenWithContainer(requireContext(), BookTopicFragment::class.java, null)
    }

    override fun onViewAllClicked(type: TypeViewAllDiscovery) {
        when (type) {
            TypeViewAllDiscovery.FIRST_LISTEN -> Bundle().apply {
                putInt(Constants.KEY_ADAPTER_TOPIC, Constants.VALUE_ADAPTER_TOPIC_1)
                openFragment(R.id.frameDiscovery, TopicDetailsFragment::class.java, this, true)
                (activity as MainActivity).hideBottomNav()
            }

            TypeViewAllDiscovery.BOOK_TRENDING -> {
                Bundle().apply {
                    putInt(Constants.KEY_ADAPTER_TRENDING, Constants.VALUE_ADAPTER_TRENDING_1)
                    openFragment(R.id.frameDiscovery, RankAudioBookFragment::class.java, this, true)
                    (activity as MainActivity).hideBottomNav()
                }
            }

            TypeViewAllDiscovery.POD_COURSE_TRENDING -> activity?.let {
                if (it is MainActivity) {
                    it.showFragment(Constants.INDEX_2, PodCourseFragment::class.java)
                }
            }

            TypeViewAllDiscovery.BEST_SALE_BOOK -> {
                openFragment(R.id.frameDiscovery, TopicDetailsFragment::class.java, null, true)
                (activity as MainActivity).hideBottomNav()
            }

            TypeViewAllDiscovery.RECOMMEND_POD_COURSE -> activity?.let {
                if (it is MainActivity) {
                    it.showFragment(Constants.INDEX_2, PodCourseFragment::class.java)
                }
            }

            TypeViewAllDiscovery.CATEGORY_BOOK -> {
                openFragment(R.id.frameDiscovery, CategoryAudiobookFragment::class.java, null, true)
                (activity as MainActivity).hideBottomNav()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        discoveryAdapter.unRegisterListener()
    }

    private fun listNewAndHot(): ArrayList<CommonData> {
        return arrayListOf(
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2016/11/19/15/18/woman-1839798_960_720.jpg",
                type = 2
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
                type = 2
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
                type = 2
            ),
        )
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
            CategoryAudiobook(id = 9, name = "Tam ly hoc")
        )
    }
}