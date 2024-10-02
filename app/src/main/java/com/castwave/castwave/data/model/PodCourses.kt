package com.castwave.castwave.data.model

data class PodCourses(
    val title: String,
    val author: String?=null,
    val imageUrl: String,
    val videoUrl: String,
    val rateStar: Float?=null,
    val isPodCourse: Boolean,
    val isNewBook: Boolean,
    val rank: Int,
    val type: Int
)
