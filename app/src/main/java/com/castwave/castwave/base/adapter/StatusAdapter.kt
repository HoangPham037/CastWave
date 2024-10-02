package com.castwave.castwave.base.adapter


sealed class StatusAdapter(val itemType: Int) {
    companion object {
        const val TYPE_LOADING = Int.MAX_VALUE - 1
        const val TYPE_NONE = Int.MAX_VALUE - 5
        const val TYPE_TO_ITEM = Int.MAX_VALUE - 6
        val RANGE_TYPE: IntRange = TYPE_NONE..TYPE_LOADING

    }
    class None : StatusAdapter(TYPE_NONE)
}