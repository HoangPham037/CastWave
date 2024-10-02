package com.castwave.castwave.ui.fragment.search.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.castwave.castwave.databinding.BottomSheetTimerBinding
import com.castwave.castwave.utils.Constants
import com.castwave.castwave.utils.extension.setVisibility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetTimer(private val minValue : Int ,private val event: (Int, Int) -> Unit) :
    BottomSheetDialogFragment(), NumberPicker.OnValueChangeListener {
    private var newMinute = minValue
    private var newSecond = Constants.INDEX_0
    private lateinit var binding: BottomSheetTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTimerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTurnOffTimer.setVisibility(minValue == Constants.INDEX_0)
        binding.minPicker.setMinValue(minValue)
        binding.secPicker.setMinValue(Constants.INDEX_0)
        binding.minPicker.setMaxValue(Constants.INDEX_59)
        binding.secPicker.setMaxValue(Constants.INDEX_59)
        binding.minPicker.value = newMinute
        binding.secPicker.value = newSecond
        binding.minPicker.setOnValueChangedListener(this)
        binding.secPicker.setOnValueChangedListener(this)
        binding.minPicker.displayedValues = getValue(minValue)
        binding.secPicker.displayedValues = getValue(Constants.INDEX_0)
        binding.tvSave.setOnClickListener {
            event.invoke(newMinute, newSecond)
            dismiss()
        }
        binding.tvTurnOffTimer.setOnClickListener {
            event.invoke(Constants.INDEX_0 , Constants.INDEX_0)
            dismiss()
        }
    }

    private fun getValue(value : Int): Array<String> {
      return  (value..Constants.INDEX_59).map {
            it.toString().padStart(Constants.INDEX_2, '0')
        }.toTypedArray()
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        picker?.let {
            when (it) {
                binding.minPicker -> newMinute = newVal
                binding.secPicker -> newSecond = newVal
            }
        }
    }
}