package com.castwave.castwave.ui.fragment.book

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.castwave.castwave.data.model.Evaluate
import com.castwave.castwave.data.model.SubCategory
import com.castwave.castwave.databinding.BottomSheetNewReviewsBinding
import com.castwave.castwave.ui.adapter.TopicCommonAdapter
import com.castwave.castwave.utils.extension.onTextChanged
import com.castwave.castwave.utils.extension.setVisibility
import com.castwave.castwave.utils.showSoftKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialogNewReviews(private val evaluate: Evaluate?) :
    BottomSheetDialogFragment() {
    private val adapter by lazy { TopicCommonAdapter() }
    private var ratingBarVoice = 5f
    private var ratingBarContent = 5f
    private var tvReviews = ""
    private lateinit var binding: BottomSheetNewReviewsBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { bottomSheet ->
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                setupFullHeight(bottomSheet)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetNewReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        setupAdapter()
        initAction()
    }

    private fun setupEdittext(text: String?) {
        text?.let {
            tvReviews = text
            binding.tbTextSize.setVisibility(text.isNotEmpty())
            if (text.length >= 500) {
                binding.edtComment.setText(text.subSequence(0, 500))
                binding.edtComment.setSelection(500)
            }
            binding.tvTotalReview.text = String.format("${text.length}/500")
        }
    }

    private fun updateUi() {
        evaluate?.let {
            ratingBarContent = evaluate.rateContent
            ratingBarVoice = evaluate.rateReadingVoice
            binding.edtComment.requestFocus()
            showSoftKeyboard(requireActivity())
            binding.edtComment.setText(evaluate.content)
            binding.edtComment.setSelection(evaluate.content.length)
        }
        binding.ratingBarVoice.rating = ratingBarVoice
        binding.ratingBarContent.rating = ratingBarContent
    }

    private fun initAction() {
        binding.edtComment.onTextChanged { setupEdittext(it) }
        binding.tvNote.setOnClickListener { showBottomSheetNote()
        }
        binding.ivClose.setOnClickListener { dismiss() }
        binding.ratingBarVoice.setOnRatingBarChangeListener { _, rating, _ ->
            ratingBarVoice = rating
        }
        binding.ratingBarContent.setOnRatingBarChangeListener { _, rating, _ ->
            ratingBarContent = rating
        }
        binding.tvSendReviews.setOnClickListener {
            dismiss()
            Toast.makeText(binding.root.context, "send reviews  nnnn", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBottomSheetNote() {
        BottomSheetNoteWhenEvaluate().show(
            childFragmentManager,
            BookDetailFragment::class.java.name
        )
    }

    private fun setupAdapter() {
        binding.rcvReviewsTitle.adapter = adapter
        adapter.items = getList()
    }

    private fun getList(): MutableList<SubCategory> {
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
}