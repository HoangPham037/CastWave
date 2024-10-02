package com.castwave.castwave.data.model

data class PodCastItem(
    val carouselPodCast: CarouselPodCast,
    val topTrendingChannel: TopTrendingChannel,
    val topTrendingEpisodes: TopTrendingEpisodes,
    val listenedRecently: ListenedRecently,
    val channelFollow: ChannelFollow,
    val likedTopic: LikedTopic,
    val categoryPodCourse: CategoryPodcast,
)
data class CarouselPodCast(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val carouselPodCast: ArrayList<CommonData>
)

data class TopTrendingChannel(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val topTrendingChannel: ArrayList<PodCast>
)
data class TopTrendingEpisodes(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val topTrendingEpisodes: ArrayList<PodCast>
)


data class ListenedRecently (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val listenedRecently: ArrayList<PodCast>
)
data class ChannelFollow (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val channelFollow: ArrayList<PodCast>
)
data class LikedTopic (
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val likedTopic: ArrayList<PodCast>
)

data class CategoryPodcast(
    val id: Int,
    val titleHeader: String? = null,
    val desc: String?=null,
    val categoryPodcast: ArrayList<CategoryAudiobook>
)
