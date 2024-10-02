package com.castwave.castwave.data.model

data class Evaluate(
    val id: Int,
    val avatar: Int,
    val name: String,
    val date: String,
    val listIdLike: ArrayList<Int>,
    val rateContent: Float,
    val rateReadingVoice: Float,
    val content: String
)
