package com.castwave.castwave.ui.fragment.library

import com.castwave.castwave.data.model.Library

data class CategoryLibrarys(
    val title: String,
    val image: Int,
    val library: MutableList<Library>?=null
)
