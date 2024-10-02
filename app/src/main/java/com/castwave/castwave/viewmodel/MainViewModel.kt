package com.castwave.castwave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress
    fun updateProgress(progressNew: Int) {
        _progress.postValue(progressNew)
    }
}