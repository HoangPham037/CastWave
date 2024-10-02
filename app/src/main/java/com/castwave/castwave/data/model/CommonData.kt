package com.castwave.castwave.data.model

data class CommonData(
    val title: String?= null,
    val author: String?=null,
    val imageUrl: String,
    val videoUrl: String ?=null,
    val rateStar: Float?=null,
    val type: Int
)
