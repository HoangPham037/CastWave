package com.castwave.castwave.data.model

data class PodCourseItem(
    val carouselPodCourse: CarouselPodCourse,
    val podCourseForYou: PodCourseForYou,
    val topTrending: TopTrending,
    val topicPodCourse: TopicPodCourse,
    val recommendMentor: RecommendMentor,
    val categoryPodCourse: CategoryBook,
)

data class CarouselPodCourse(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val carouselPodCourse: ArrayList<CommonData>
)

data class PodCourseForYou (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val forYous: ArrayList<PodCourses>
)

data class TopTrending(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val topTrending: ArrayList<PodCourses>
)

data class TopicPodCourse (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val topicPodCourse: ArrayList<PodCourses>
)

data class RecommendMentor (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val recommendMentor: ArrayList<Mentor>
)
