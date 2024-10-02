package com.castwave.castwave.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castwave.castwave.data.local.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopicBookDetailsViewModel @Inject constructor(private val preferences: Preferences): ViewModel() {
     val isDisplayList : MutableLiveData<Boolean> = MutableLiveData(getDefaultBoolean())
    fun updateValue(newIsDisplayList : Boolean) {
        isDisplayList.value = newIsDisplayList
        setDisplayList(newIsDisplayList)
    }
    private fun getDefaultBoolean(): Boolean {
        return preferences.getBoolean("isDisplayList")
    }
    private fun setDisplayList(newIsDisplayList: Boolean) {
        preferences.setBoolean("isDisplayList", newIsDisplayList)
    }
}