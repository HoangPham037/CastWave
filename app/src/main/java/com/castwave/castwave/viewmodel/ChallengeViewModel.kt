package com.castwave.castwave.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor() : ViewModel() {
    fun getTime(): Int {
        val calendar: Calendar = Calendar.getInstance()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        return 24 - hour
    }
}