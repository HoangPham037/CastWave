package com.castwave.castwave.ui.fragment.search

import android.os.Bundle
import android.util.Log
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.rx.Buser
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.databinding.FragmentSearchBinding
import com.castwave.castwave.ui.activity.MainActivity
import com.castwave.castwave.ui.fragment.discovery.DiscoveryFragment
import com.castwave.castwave.ui.fragment.discovery.TopicDetailsFragment
import com.castwave.castwave.ui.fragment.search.children.ChildrenDetailFragment
import com.castwave.castwave.ui.fragment.search.meditation.MeditationSeriesFragment
import com.castwave.castwave.ui.fragment.search.podcast.PodcastChannelFragment
import com.castwave.castwave.ui.fragment.search.podcast.PodcastFragment
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.getStatusBarHeight
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.openScreenWithContainer

class SearchFragment : BaseFragment<FragmentSearchBinding>(), RxBus.OnMessageReceived {

    private val adapter by lazy { CategorySearchAdapter() }
    override fun getLayoutBinding(): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        rxBus.registerBuser(this)
        binding.rcvCategorySearch.setHasFixedSize(true)
        binding.rcvCategorySearch.adapter = adapter
        adapter.items = listCategorySearch()
        adapter.setOnItemClickListener { categorySearch, pos ->
            binding.viewSpace.setVisibility(pos == adapter.itemCountZ - Constants.INDEX_1)
            categorySearch?.let { nextFragment(categorySearch) }
        }
        binding.imgStore.clickWithDebounce {
            openFragment(R.id.frameSearch, TopicDetailsFragment::class.java, null, true)
        }
        binding.imgUser.setOnClickListener {
        }
        val headerPaddingTop = (context?.getStatusBarHeight()
            ?: requireContext().resources.getDimension(R.dimen._30dp)).toInt()
        binding.toolbarLayout.setPadding(
            binding.toolbarLayout.paddingLeft,
            headerPaddingTop,
            binding.toolbarLayout.paddingRight,
            binding.toolbarLayout.paddingBottom
        )
        binding.collapsingToolbarLayout.setContentScrimResource(R.drawable.bgr_gradient_header)
    }

    private fun nextFragment(categorySearch: CategorySearch) {
        when (categorySearch.title) {
            "THIỀN" -> showFragment(MeditationSeriesFragment::class.java)
            "THIẾU NHI" -> showFragment(ChildrenDetailFragment::class.java)
            "PODCAST" -> showFragment(PodcastFragment::class.java)
        }
    }

    private fun showFragment(fragment: Class<*>) {
        openScreenWithContainer(requireContext(), fragment, null)
    }

    private fun listCategorySearch(): ArrayList<CategorySearch> {
        return arrayListOf(
            CategorySearch("SÁCH NÓI", R.drawable.noto_lotus),
            CategorySearch("PODCOURSE", R.drawable.noto_lotus),
            CategorySearch("TRUYỆN NGỦ", R.drawable.noto_lotus),
            CategorySearch("TÓM TẮT SÁCH", R.drawable.noto_lotus),
            CategorySearch("NHẠC CHỦ ĐỀ", R.drawable.noto_lotus),
            CategorySearch("THIỀN", R.drawable.noto_lotus),
            CategorySearch("THIẾU NHI", R.drawable.noto_lotus),
            CategorySearch("EBOOK", R.drawable.noto_lotus),
            CategorySearch("PODCAST", R.drawable.noto_lotus)
        )
    }

    override fun onMessageReceived(buser: Buser?) {
        if (buser?.values == Constants.VALUE_BACK_FROM_SEARCH) {
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
}