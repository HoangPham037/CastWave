package com.castwave.castwave.ui.fragment.category

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.castwave.castwave.R
import com.castwave.castwave.base.BaseFragment
import com.castwave.castwave.data.model.CategoryAudiobook
import com.castwave.castwave.databinding.FragmentCategoryAudiobookBinding
import com.castwave.castwave.ui.adapter.CategoryAudiobookAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryAudiobookFragment : BaseFragment<FragmentCategoryAudiobookBinding>() {
    private val adapter by lazy { CategoryAudiobookAdapter() }
    override fun updateUI(savedInstanceState: Bundle?) {
        initView()
        initAction()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rcvCategory.adapter = adapter
        adapter.items = getData()
    }

    private fun getData(): MutableList<CategoryAudiobook> {
        return arrayListOf(
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
            CategoryAudiobook(1, "Tâm lý học", R.drawable.ic_store),
        )
    }

    private fun initAction() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        activity?.setActionBar(binding.mainToolbar)
        val spannableString = SpannableString("Danh mục sách nói")
        spannableString.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        activity?.actionBar?.title = spannableString
    }

    override fun getLayoutBinding(): FragmentCategoryAudiobookBinding =
        FragmentCategoryAudiobookBinding.inflate(layoutInflater)
}