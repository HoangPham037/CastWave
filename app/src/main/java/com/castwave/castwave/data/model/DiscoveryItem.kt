package com.castwave.castwave.data.model

data class DiscoveryItem(
    val newAndHot: NewAndHot,
    val rankingPodCourse: RankingPodCourse,
    val firstListenBook: FirstListenBook,
    val rankingBook: RankingBook,
    val podCoursesRecommend: PodCoursesRecommend,
)

data class NewAndHot(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val newAndHot: ArrayList<CommonData>
)

data class RankingPodCourse(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val rankingPodCourse: ArrayList<PodCourses>
)

data class BestSaleBook(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val bestSaleBook: ArrayList<Book>
)

data class FirstListenBook(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val firstListenBook: ArrayList<Book>
)

data class RankingBook(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String? = null,
    val rankingBook: ArrayList<Book>
)

data class PodCoursesRecommend(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val podCoursesRecommend: ArrayList<PodCourses>
)

data class ListBook(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val listBook: ArrayList<Book>
)

data class ListCategory(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val listCategory: ArrayList<CategoryAudiobook>
)
data class CategoryBook(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val categoryBook: ArrayList<CategoryAudiobook>
)

