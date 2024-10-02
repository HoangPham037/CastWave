package com.castwave.castwave.ui.fragment.search.meditation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.castwave.castwave.data.model.Song
import com.castwave.castwave.databinding.BottomSheetChooseSoundBinding
import com.castwave.castwave.ui.adapter.SoundAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetChooseSound(
    private val title: String,
    private val listSound: ArrayList<Song>,
    private val event: (Int) -> Unit
) :
    BottomSheetDialogFragment() {
    private var id = 0
    private val adapter by lazy { SoundAdapter {
        id = it
    } }
    private lateinit var binding: BottomSheetChooseSoundBinding

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
        binding = BottomSheetChooseSoundBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvSound.adapter = adapter
        adapter.items = listSound
        binding.tvTitle.text = title
        binding.tvSave.setOnClickListener {
            event.invoke(id)
            dismiss()
        }
    }
}