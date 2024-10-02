package com.castwave.castwave.data.model

data class PodCast(
    val id : Int ,
    val title: String,
    val author: String?=null,
    val imageUrl: String,
    val videoUrl: String,
    val time: Long,
    val isPodCast: Boolean,
    val isNewBook: Boolean,
    val rank: Int,
    val type: Boolean
)
