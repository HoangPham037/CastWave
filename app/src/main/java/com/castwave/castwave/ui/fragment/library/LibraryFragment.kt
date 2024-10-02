package com.castwave.castwave.ui.fragment.library

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.data.model.Library
import com.castwave.castwave.databinding.FragmentLibraryBinding
import com.castwave.castwave.databinding.PopupDialogArrangeBinding
import com.castwave.castwave.ui.adapter.LibraryAdapter
import com.castwave.castwave.utils.extension.clickWithDebounce
import com.castwave.castwave.utils.extension.getStatusBarHeight
import com.castwave.castwave.utils.extension.hide
import com.castwave.castwave.utils.extension.show
import kotlin.random.Random

class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {
    private val adapter by lazy { CategoryLibrary() }
    private val adapterLibrary by lazy { LibraryAdapter() }
    override fun getLayoutBinding(): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        val headerPaddingTop =
            context?.getStatusBarHeight()
                ?: requireContext().resources.getDimension(R.dimen._30dp).toInt()
        binding.toolbarLayout.setPadding(
            binding.toolbarLayout.paddingLeft,
            headerPaddingTop,
            binding.toolbarLayout.paddingRight,
            binding.toolbarLayout.paddingBottom
        )
        binding.collapsingToolbarLayout.setContentScrimResource(R.drawable.bgr_gradient_header)
        binding.rcvCategoryLibrary.adapter = adapter
        adapter.items = listItemCategory()
        adapter.setOnItemClickListener { item, _ ->
            binding.tvTitleItem.text = item?.title
            binding.rcvLibrary.adapter = adapterLibrary
            if (item?.library != null) {
                binding.layoutEmptyData.layoutEmpty.hide()
                binding.rcvLibrary.show()
                adapterLibrary.items = item.library
            } else {
                binding.layoutEmptyData.layoutEmpty.show()
                binding.rcvLibrary.hide()
            }
        }
        binding.vRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                adapterLibrary.items.shuffle(Random(System.currentTimeMillis()))
                adapterLibrary.notifyDataSetChanged()
                binding.vRefresh.isRefreshing = false
            }, 2000)
        }

        binding.imgSort.clickWithDebounce {
            val popup = BaseShowPopup(requireContext(), PopupDialogArrangeBinding::inflate)
            popup.show(binding.imgSort, -320, 0)
        }
    }

    private fun listItemLibrary(): ArrayList<Library> {
        return arrayListOf(
            Library(
                "Thieu nhi",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "Ebook",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                true
            ),
            Library(
                "Tom tat sach",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "Thien",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "yeu thich",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                true
            ),
            Library(
                "truyen ngu",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "nhac chu de",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "yeu thich",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "yeu thich",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
            Library(
                "yeu thich",
                "Spaiens: luoc su loai nguoi luoc su loai nguoi luoc su loai nguoi",
                "Pham Van Hoang",
                false
            ),
        )
    }


    private fun listItemCategory(): ArrayList<CategoryLibrarys> {
        return arrayListOf(
            CategoryLibrarys("Tat ca", R.drawable.ic_all, listItemLibrary()),
            CategoryLibrarys("Sach noi", R.drawable.ic_all, null),
            CategoryLibrarys("Thien", R.drawable.ic_all, listItemLibrary()),
            CategoryLibrarys("PodCourse", R.drawable.ic_all, null),
            CategoryLibrarys("Thieu nhi", R.drawable.ic_all, null),
            CategoryLibrarys("Truyen ngu", R.drawable.ic_all, null),
            CategoryLibrarys("ebook", R.drawable.ic_all, listItemLibrary()),
            CategoryLibrarys("Poscast", R.drawable.ic_all, null),
            CategoryLibrarys("Yeu thich", R.drawable.ic_all, null),
        )
    }
}