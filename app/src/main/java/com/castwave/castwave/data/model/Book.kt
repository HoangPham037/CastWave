package com.castwave.castwave.data.model

data class Book(
    val id: Int,
    val isFree: Boolean,
    val imgUrl: String,
    val name: String,
    val author: String,
    val rank: Int,
    val date : String
)
