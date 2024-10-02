package com.castwave.castwave.base

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.castwave.castwave.R
import com.castwave.castwave.databinding.BaseDialogBinding
import com.castwave.castwave.utils.extension.clickWithDebounce

class DialogConfirm(
    context: Context,
    styleRes: Int,
    private val resImage: Int,
    private val header: String,
    private val desc: String,
    private val textSubmit: String,
    private val textCancel: String,
    private val event: () -> Unit
) : BaseDialog(context, styleRes) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = BaseDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window?.setBackgroundDrawableResource(R.color.transparent)
        setCancelable(false)
        binding.tvHeaderDialog.text = header
        binding.tvDescDialog.text = desc
        binding.tvSubmit.text = textSubmit
        binding.tvCancle.text = textCancel
        binding.imgHeader.setBackgroundResource(resImage)
        binding.tvCancle.clickWithDebounce {
            dismiss()
        }
        binding.tvSubmit.clickWithDebounce {
            event.invoke()
            dismiss()
        }
    }
}