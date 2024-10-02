package com.castwave.castwave.ui.fragment.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.castwave.castwave.databinding.BottomSheetNoteWhenEvaluateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetNoteWhenEvaluate :
    BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetNoteWhenEvaluateBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetNoteWhenEvaluateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvUnderstood.setOnClickListener {
            dismiss()
        }
    }
}