package com.castwave.castwave.ui.fragment.search.podcast

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.marginLeft
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.CarouselPodCast
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.data.model.CategoryBook
import com.castwave.castwave.data.model.ChannelFollow
import com.castwave.castwave.data.model.CommonData
import com.castwave.castwave.data.model.LikedTopic
import com.castwave.castwave.data.model.ListenedRecently
import com.castwave.castwave.data.model.PodCast
import com.castwave.castwave.data.model.TopTrendingChannel
import com.castwave.castwave.data.model.TopTrendingEpisodes
import com.castwave.castwave.databinding.FragmentPodcastBinding
import com.castwave.castwave.ui.adapter.DiscoveryAdapter
import com.castwave.castwave.ui.adapter.PodcastAdapter
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_CATEGORY
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_CHANNEL_FOLLOW
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_LIKED_TOPIC
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_LISTENED_RECENTLY
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_NEW_AND_TOP_TRENDING
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_TOP_TRENDING_CHANNEL
import com.castwave.castwave.ui.adapter.PodcastAdapter.Companion.TYPE_TOP_TRENDING_EPISODES
import com.castwave.castwave.utils.extension.getStatusBarHeight
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PodcastFragment : BaseFragment<FragmentPodcastBinding>() {

    private val adapter by lazy { PodcastAdapter(mutableListOf()) }
    override fun getLayoutBinding(): FragmentPodcastBinding {
        return FragmentPodcastBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
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
        binding.rcvPodCast.setHasFixedSize(true)
        binding.rcvPodCast.adapter = adapter
        adapter.setEvent{ type, data ->
            clickView(type , data)
        }
        adapter.showData(
            CarouselPodCast(
                1,
                carouselPodCast = listNewAndHot()
            ),
            TopTrendingChannel(
                2,
                titleHeader = "Top kênh thịnh hành",
                topTrendingChannel = listPodCast()
            ),
            TopTrendingEpisodes(
                1,
                "Top tập Podcast thịnh hành",
                "Podcourse",
                topTrendingEpisodes = listPodCast()
            ),
            ListenedRecently(
                1,
                "Các tập mới từ",
                "Kênh đang theo dõi",
                listenedRecently = listPodCast()
            ),

            LikedTopic(
                1,
                "Phát triển bản thân",
                "Từ chủ đề bạn thích",
                likedTopic = listPodCast()
            ),
            ChannelFollow(
                1,
                "Kênh đang theo dõi",
                "Các tập mới từ",
                channelFollow = listPodCast()
            ),

            CategoryBook(
                1,
                titleHeader = "DANH MỤC PODCAST",
                categoryBook = listCategoryAudioBook()
            )
        )
        binding.rcvPodCast.recycledViewPool.setMaxRecycledViews(
            DiscoveryAdapter.TYPE_POD_COURSE_NEW,
            1
        )
    }
    private fun clickView(type: Int, data: Any?) {
        when(type){
            TYPE_NEW_AND_TOP_TRENDING ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_TOP_TRENDING_CHANNEL ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_TOP_TRENDING_EPISODES ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_LISTENED_RECENTLY ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_CHANNEL_FOLLOW ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_LIKED_TOPIC ->{
                data?.let {
                    openScreenWithContainer(requireContext() , PodcastChannelFragment::class.java , null)
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
            TYPE_CATEGORY ->{
                data?.let {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW DETAILLLLLLLLLLLLLLL")
                }?:run {
                    Log.d("TAG", "setupAdapter: CHANNNNNNNNNNNNNNNNN SHOW ALLLLLLLLLLLLLLLLLLL")
                }
            }
        }
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
                type = 3
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg",
                type = 3
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/11/19/21/10/glasses-1052010_1280.jpg",
                type = 3
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/12/04/09/13/leaves-1076307_1280.jpg",
                type = 3
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2016/11/20/08/58/books-1842261_1280.jpg",
                type = 3
            ),
            CommonData(
                "Milk and honey",
                "Pham Van Hoang",
                "https://cdn.pixabay.com/photo/2015/11/19/21/14/glasses-1052023_1280.jpg",
                type = 3
            ),
        )
    }

  private  val currentTimeMillis: Long = System.currentTimeMillis()

    private fun listPodCast(): ArrayList<PodCast> {
        return arrayListOf(
            PodCast(
                id = 1,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://danviet.mediacdn.vn/296231569849192448/2022/7/18/z3575814842290f4a344b1db566b37fa96b7a2550a9801-1658133352416631936514.jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),
            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://media-cdn-v2.laodong.vn/Storage/NewsPortal/2021/10/30/969136/Cristiano-Ronaldo4.jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://aobongda.net/pic/Images/Module/News/images/4(3).jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://cafebiz.cafebizcdn.vn/2019/1/18/photo-1-154777686469242085471.jpeg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://photo.znews.vn/w660/Uploaded/kbd_pilk/2021_08_20/78.jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://image.dienthoaivui.com.vn/x,webp,q90/https://dashboard.dienthoaivui.com.vn/uploads/dashboard/editor_upload/hinh-nen-messi-4k-10.jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://danviet.mediacdn.vn/296231569849192448/2022/7/18/z3575814842290f4a344b1db566b37fa96b7a2550a9801-1658133352416631936514.jpg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),

            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://dienbientv.vn/dataimages/202108/original/images3067570_c75cd207_e3b1_4dc7_a5f5_431c0b99834c_1628203657304198409151.jpeg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),
            PodCast(
                id = 2,
                title = "Milk and honey",
                author = "Pham van hoang",
                imageUrl = "https://nld.mediacdn.vn/291774122806476800/2023/2/6/2486eec2-d846-41d0-ab19-4b348d63b5e1-16756864512721777446481.jpeg",
                videoUrl = "link video",
                time = currentTimeMillis,
                isPodCast = true,
                isNewBook = true,
                rank = 1,
                type = false
            ),
        )
    }
}