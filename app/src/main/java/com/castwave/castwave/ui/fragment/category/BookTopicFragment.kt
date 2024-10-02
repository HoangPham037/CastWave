package com.castwave.castwave.ui.fragment.category

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.base.BaseShowPopup
import com.castwave.castwave.data.model.Badge
import com.castwave.castwave.data.model.Book
import com.castwave.castwave.data.model.BookTopic
import com.castwave.castwave.data.model.SubCategory
import com.castwave.castwave.databinding.FragmentBookTopicBinding
import com.castwave.castwave.databinding.PopupDialogArrangeBinding
import com.castwave.castwave.ui.adapter.BookTopicAdapter
import com.castwave.castwave.ui.adapter.CategoryTopicAdapter
import com.castwave.castwave.ui.fragment.book.BookDetailFragment
import com.castwave.castwave.utils.collapse
import com.castwave.castwave.utils.expand
import com.castwave.castwave.utils.openScreenWithContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookTopicFragment : BaseFragment<FragmentBookTopicBinding>() {
    private val adapter by lazy { BookTopicAdapter() }
    private val adapterCategory by lazy { CategoryTopicAdapter() }
    private var  listItem :ArrayList<Book> = arrayListOf()
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        setupAdapter()
    }
    private fun initView() {
        activity?.setActionBar(binding.mainToolbar)
        val spannable = SpannableString("Tâm lí hoc")
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        spannable.setSpan(ForegroundColorSpan(color), 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        activity?.actionBar?.title = spannable
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.ivArrange.setOnClickListener { view ->
            openPopup(view)
        }
        adapter.setOnItemClickListener { _, _ ->
            openScreenWithContainer(requireContext() , BookDetailFragment::class.java , null)
        }
        binding.tvSubCategories.setOnClickListener {
            expand(binding.lnSubCategory)
        }
        binding.ivClose.setOnClickListener {
            collapse(binding.lnSubCategory)
        }
    }
    private fun setupAdapter() {
        binding.rcvBookTopic.adapter = adapter
        binding.rcvCategoryTopic.adapter = adapterCategory
        listItem = listBook()
        adapter.items = listItem
        adapterCategory.items = getListTopic()
    }
    private fun getListTopic(): MutableList<SubCategory> {
        return arrayListOf(
            SubCategory(1 , "Tâm lý học tội phạm"),
            SubCategory(1 , "Phân tâm học"),
            SubCategory(1 , "Tiềm thức & thôi miên"),
            SubCategory(1 , "Giải mã giấc mơ"),
            SubCategory(1 , "Tâm lý học hành vi"),
            SubCategory(1 , "Sức khỏe tâm lý"),
            SubCategory(1 , "Tâm lý học xã hội"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức"),
            SubCategory(1 , "Tâm lý học nhận thức")
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

    private fun openPopup(view: View) {
        val popupDialog = BaseShowPopup(requireContext(), PopupDialogArrangeBinding::inflate)
        popupDialog.show(view, -300, -50, Gravity.BOTTOM)
        popupDialog.binding.tvOldest.setOnClickListener {
            listItem.sortBy { it.date }
            popupDialog.dismiss()
            adapter.notifyDataSetChanged()
        }
        popupDialog.binding.tvInformation.setOnClickListener {
            listItem.sortByDescending { it.date }
            popupDialog.dismiss()
            adapter.notifyDataSetChanged()
        }
        popupDialog.binding.tvPopular.setOnClickListener {
            listItem.sortByDescending { it.date }
            popupDialog.dismiss()
            adapter.notifyDataSetChanged()
        }
    }



    override fun getLayoutBinding(): FragmentBookTopicBinding =
        FragmentBookTopicBinding.inflate(layoutInflater)
}