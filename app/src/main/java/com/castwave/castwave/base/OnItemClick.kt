package com.castwave.castwave.base

fun interface OnItemClick<T : Any> {
    fun onItemClickListener(item: T?, position: Int)
}

interface PodCourseItemClickListener {
    fun onViewAllClicked(type: TypeViewAllPodCourse)
    fun onGoVideoInfoMentor()
}

interface PodCourseClickListener {
    fun onPodCourseClicked(type: TypeCarousel)
    fun onPlayPodCourseClicked()
}

interface CategoryBookListener {
    fun categoryClicked()
}

interface DiscoveryItemClickListener {
    fun onBookClicked()
    fun onViewAllClicked(type: TypeViewAllDiscovery)
}

enum class TypeCarousel {
    POD_COURSE,
    AUDIO_BOOK
}

enum class TypeViewAllDiscovery {
    FIRST_LISTEN,
    BOOK_TRENDING,
    POD_COURSE_TRENDING,
    BEST_SALE_BOOK,
    RECOMMEND_POD_COURSE,
    CATEGORY_BOOK,
}

enum class TypeViewAllPodCourse {
    TOP_TRENDING,
    TOPIC,
    CATEGORY
}